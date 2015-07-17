package org.g6.laas.sm.file.sorter;


import org.g6.laas.core.exception.LaaSCoreRuntimeException;
import org.g6.laas.core.field.DateTimeField;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.file.sorter.AbstractLogFileSorter;
import org.g6.laas.core.format.DefaultFormatFactory;
import org.g6.laas.core.format.DefaultInputFormat;
import org.g6.laas.core.log.line.LogLine;
import org.g6.laas.core.log.reader.LogFileReader;
import org.g6.laas.core.log.result.SplitResult;

import java.io.IOException;
import java.util.Comparator;

public class RTELogFileSorter extends AbstractLogFileSorter{

    @Override
    protected Comparator<ILogFile> getComparator() {
        return new RTELogFileComparator();
    }

    public static class RTELogFileComparator implements Comparator<ILogFile>{
        @Override
        public int compare(ILogFile logFile1, ILogFile logFile2) {
            if(logFile1.isValid() && logFile2.isValid()){
                try{
                    DateTimeField dateTime1 = getEntryDateTime(logFile1);
                    DateTimeField dateTime2 = getEntryDateTime(logFile2);
                    return dateTime1.compareTo(dateTime2);
                }catch(IOException e){
                    throw new LaaSCoreRuntimeException("Sort RTE files failed");
                }
            }else
                throw new LaaSCoreRuntimeException("Invalid SM RTE File can NOT be sorted");
        }

        private DateTimeField getEntryDateTime(ILogFile logFile) throws IOException{
            LogFileReader reader = new LogFileReader(logFile);
            LogLine line1 = new LogLine();
            line1.setContent(reader.readLine());
            line1.setInputFormat(DefaultFormatFactory.getInputFormat("SMRTE_SM_LOG"));
            SplitResult result = line1.split();
            return (DateTimeField)result.get("datetime");
        }
    }
}
