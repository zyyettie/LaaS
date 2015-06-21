package org.g6.laas.core.log;

import lombok.NoArgsConstructor;
import org.g6.laas.core.exception.InputFormatNotFoundException;
import org.g6.laas.core.exception.LaaSRuntimeException;
import org.g6.laas.core.field.DoubleField;
import org.g6.laas.core.field.Field;
import org.g6.laas.core.field.IntegerField;
import org.g6.laas.core.field.TextField;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.format.FieldFormat;
import org.g6.laas.core.format.InputFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class LogLine extends Line {
    private String sortValue;

    public LogLine(ILogFile file, String content, int lineNumber) {
        super(file, content, lineNumber, null);
    }

    public LogLine(ILogFile file, String content, int lineNumber, InputFormat lineFormat) {
        super(file, content, lineNumber, lineFormat);
        split();
    }

    @Override
    public Collection<Field> split() {
        if (isSplitable()) {
            List<FieldFormat> fieldFormatList = null;
            List<String> errorKeyList = new ArrayList<>();
            int counter = 0;
            //here if only split with separator, the line format should not be special
            Map<String, List<FieldFormat>> lineFormats = format.getFormats();
            for (Map.Entry<String, List<FieldFormat>> entry : lineFormats.entrySet()) {
                if (getContent().contains(entry.getKey())) {
                    errorKeyList.add(entry.getKey());
                    counter++;
                    fieldFormatList = entry.getValue();
                }
            }
            if (counter != 1) {
                StringBuffer sb = new StringBuffer();
                sb.append("Found more than one formats for the line which number is ")
                        .append(getLineNumber())
                        .append(" in ")
                        .append(getFile().getName())
                        .append(". The key words matched are: ");
                for (String key : errorKeyList) {
                    sb.append(key)
                            .append(", ");
                }
                //TODO remove the last comma
                throw new LaaSRuntimeException(sb.toString());
            }

            if (fieldFormatList == null)
                throw new InputFormatNotFoundException("InputFormat not found");

            String[] fieldContents = getContent().split(format.getSeperator());
            Collection<Field> fieldList = new ArrayList<>();

            for (int i = 0; i < fieldContents.length; i++) {
                Field f = null;
                FieldFormat ff = fieldFormatList.get(i);
                String fieldFormatType = ff.getName();

                if (fieldFormatType.equals("String")) {
                    if (ff.isSplitable()) {
                        String[] secSplitValues = fieldContents[i].split(ff.getSeperator());
                        f = new TextField(secSplitValues[ff.getIndexOfValue()]);
                        this.sortValue = secSplitValues[ff.getIndexOfValue()];
                    } else {
                        f = new TextField(fieldContents[i]);
                    }
                } else if (fieldFormatType.equals("Integer")) {
                    f = new IntegerField(fieldContents[i]);
                } else if (fieldFormatType.equals("Date")) {
                    //TODO
                } else if (fieldFormatType.equals("Time")) {

                } else if (fieldFormatType.equals("DateTime")) {

                } else if (fieldFormatType.equals("Double")) {
                    f = new DoubleField(fieldContents[i]);
                }
                fieldList.add(f);
            }
            return fieldList;
        }

        return null;
    }

    @Override
    public int compareTo(Object o) {
        //TODO here need to check which type of value will be used to compare. e.g. double, String, Date
        return 0;
    }

    public String getSortValue() {
        return sortValue;
    }
}
