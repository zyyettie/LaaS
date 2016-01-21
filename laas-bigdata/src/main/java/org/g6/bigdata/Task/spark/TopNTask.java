package org.g6.bigdata.Task.spark;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.spark.api.java.JavaNewHadoopRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.g6.bigdata.log.LogLine;
import org.g6.bigdata.spark.SparkUtil;
import org.g6.util.RegexUtil;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TopNTask {

    public static void main(String[] args){
        JavaSparkContext sc = SparkUtil.getSparkContext(args[0]);
        JavaPairRDD<LongWritable, Text> javaPairRDD = sc.newAPIHadoopFile(
                args[1],
                TextInputFormat.class,
                LongWritable.class,
                Text.class,
                new Configuration()
        );
        JavaNewHadoopRDD<LongWritable, Text> hadoopRDD = (JavaNewHadoopRDD) javaPairRDD;

        JavaRDD<LogLine> lines = hadoopRDD.mapPartitionsWithInputSplit(
                new Function2<InputSplit, Iterator<Tuple2<LongWritable, Text>>, Iterator<LogLine>>() {
                    @Override
                    public Iterator<LogLine> call(InputSplit inputSplit, final Iterator<Tuple2<LongWritable, Text>> lines) throws Exception {
                        FileSplit fileSplit = (FileSplit) inputSplit;
                        final String fileName = fileSplit.getPath().getName();
                        return new Iterator<LogLine>() {
                            @Override
                            public boolean hasNext() {
                                return lines.hasNext();
                            }
                            @Override
                            public LogLine next() {
                                Tuple2<LongWritable, Text> entry = lines.next();
                                return new LogLine(entry._2().toString(), fileName);
                            }

                            @Override
                            public void remove() {
                                  //do nothing
                            }
                        };
                    }
                },
                true
        );

        JavaRDD<LogLine> words = lines.flatMap(new FlatMapFunction<LogLine, LogLine>() {
            @Override
            public Iterable<LogLine> call(LogLine line) {
                String content = line.getContent();
                String pattern = "^\\s*(\\d+)\\(\\s+(\\d+)\\)\\s+(\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+)\\s+RTE D DBQUERY(?:\\^[^\\^]+){6}\\^(\\d+\\.\\d+)";

                String[] values = RegexUtil.getValues(content, pattern);

                if(values != null) {
                    line.setSortValue(Double.parseDouble(values[3]));
                    return Arrays.asList(line);
                }
                return null;
            }
        });

        List<LogLine> lineList = words.collect();

        Collections.sort(lineList);

        int count = 0;
        for(LogLine line : lineList){
            System.out.println(line.getContent());
            count ++;
            if (count > 50)
                break;
        }
    }

}
