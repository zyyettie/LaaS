package org.g6.bigdata.Task.spark;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.g6.bigdata.hdfs.HDFSUtil;
import org.g6.bigdata.log.LogLine;
import org.g6.bigdata.spark.SparkUtil;
import org.g6.laas.core.field.DoubleField;
import org.g6.laas.core.field.Field;
import org.g6.util.RegexUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the task used for TopN based on SM RTE log files.
 * After this class is compiled and is packaged in a jar file,
 * the jar must be uploaded in Spark environment. After that, execute the command like this:
 * ./spark-submit --master yarn-client --class org.g6.bigdata.Task.spark.TopNTask \
 * --jars $(echo /home/johnson/share/*.jar | tr ' ' ',') \
 * /home/johnson/share/laas-bigdata-1.0.jar \
 * /laas/sm_dbquery.log.1 \
 * hdfs://localhost:9000/laas/topN_result.log
 * <p/>
 * Note:
 * 1. jars is used to specify all the dependency jars.
 * 2. --master get the description of all the values about this parameter in SparkUtil class.
 * 3. if --jars and --master have not to be specified in command line, refer to getSparkContext in SparkUtil
 */
@Slf4j
public class TopNTask {

    public static void main(String[] args) {
        if (args.length < 2) {
            log.error("There must be two elements in args, existing.............");
            System.exit(0);
        } else {
            log.info("the first arg is: " + args[0] + " and the second arg is: " + args[1]);
        }

        JavaSparkContext sc = SparkUtil.getSparkContext();
        JavaRDD<String> lines = sc.textFile(args[0]);

        log.info("Start doing RDD transformation");

        JavaRDD<LogLine> filterRDD = lines.flatMap(new FlatMapFunction<String, LogLine>() {
            @Override
            public Iterable<LogLine> call(String lineContent) throws Exception {
                String pattern = "^\\s*(\\d+)\\(\\s+(\\d+)\\)\\s+(\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+)\\s+RTE D DBQUERY(?:\\^[^\\^]+){6}\\^(\\d+\\.\\d+)";
                String[] values = RegexUtil.getValues(lineContent, pattern);
                List<LogLine> lineList = new ArrayList();

                if (values != null) {
                    Field field = new DoubleField(values[3]);
                    LogLine line = new LogLine(lineContent, field);
                    lineList.add(line);
                }
                return lineList;
            }
        });

        // false : descending, true : ascending
        JavaRDD<LogLine> sortRDD = filterRDD.sortBy(new Function<LogLine, Double>() {
            @Override
            public Double call(LogLine logLine) throws Exception {
                DoubleField df = (DoubleField) logLine.getSortedField();
                return df.getValue();
            }
        }, false, 1);

        List<LogLine> lineList = sortRDD.top(50);

        if (lineList != null) {
            log.info("the record size is:" + lineList.size());
        }

        new HDFSUtil().put(convert(lineList), args[1]);
    }

    private static List<String> convert(List<LogLine> lines) {
        List<String> list = new ArrayList();
        for (LogLine line : lines) {
            list.add(line.getContent() + "\r\n");
        }
        return list;
    }
}
