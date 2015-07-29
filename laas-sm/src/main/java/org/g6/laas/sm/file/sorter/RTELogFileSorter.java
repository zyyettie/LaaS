package org.g6.laas.sm.file.sorter;


import org.apache.commons.lang.StringUtils;
import org.g6.laas.core.field.DateTimeField;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.file.sorter.AbstractLogFileSorter;
import org.g6.laas.core.format.provider.DefaultInputFormatProvider;
import org.g6.laas.core.log.line.LogLine;
import org.g6.laas.core.log.reader.LogFileReader;
import org.g6.laas.core.log.result.SplitResult;
import org.g6.laas.sm.exception.SMRuntimeException;

import java.io.IOException;
import java.util.Comparator;

public class RTELogFileSorter extends AbstractLogFileSorter {

    @Override
    protected Comparator<ILogFile> getComparator() {
        return new RTELogFileComparator();
    }

    public static class RTELogFileComparator implements Comparator<ILogFile> {
        @Override
        public int compare(ILogFile logFile1, ILogFile logFile2) {
            if (logFile1.isValid() && logFile2.isValid()) {
                try {
                    DateTimeField dateTime1 = getEntryDateTime(logFile1);
                    DateTimeField dateTime2 = getEntryDateTime(logFile2);
                    return dateTime1.compareTo(dateTime2);
                } catch (IOException e) {
                    throw new SMRuntimeException("Sort RTE files failed");
                }
            } else
                throw new SMRuntimeException(logFile1.getName() + " or " + logFile2.getName() + " containing wrong format content.");

        }

        private DateTimeField getEntryDateTime(ILogFile logFile) throws IOException {
            LogFileReader reader = new LogFileReader(logFile);
            reader.open();
            LogLine line1 = new LogLine();
            String str;
            while((str = reader.readLine()) != null){
                  if(!StringUtils.isBlank(str)){
                      break;
                  }
            }

            line1.setContent(str);
            reader.close();
            line1.setInputFormat(new DefaultInputFormatProvider("SMRTE_SM_LOG", new String[]{"DEFAULT"}).getInputFormat());
            SplitResult result = line1.split();
            return (DateTimeField) result.get("datetime");
        }
    }
}
