package org.g6.laas.core.log;

import org.g6.laas.core.exception.InputFormatNotFoundException;
import org.g6.laas.core.exception.LaaSRuntimeException;
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

public class LogLine extends Line {
    public LogLine() {
    }

    public LogLine(ILogFile file, String content, int lineNumber) {
        super(file, content, lineNumber, null);
    }

    public LogLine(ILogFile file, String content, int lineNumber, InputFormat lineFormat) {
        super(file, content, lineNumber, lineFormat);
    }

    @Override
    public Collection<Field> split() {
        if (isSplitable()) {
            List<FieldFormat> list = null;
            List<String> errorKeyList = new ArrayList<String>();
            int counter = 0;
            //here if only split with separator, the line format should not be special
            Map<String, List<FieldFormat>> lineFormats = format.getFormats();
            for (Map.Entry<String, List<FieldFormat>> entry : lineFormats.entrySet()) {
                if (getContent().contains(entry.getKey())) {
                    errorKeyList.add(entry.getKey());
                    counter++;
                    list = entry.getValue();
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
                throw new LaaSRuntimeException(sb.toString());
            }

            if (list == null)
                throw new InputFormatNotFoundException("InputFormat not found");

            String[] fieldContents = getContent().split(format.getSeperator());
            Collection<Field> fieldList = new ArrayList<>();
            for (int i = 0; i < fieldContents.length; i++) {
                Field f = null;
                String fieldFormatType = list.get(i).getName();
                if (fieldFormatType.equals("String")) {
                    f = new TextField(fieldContents[i]);
                } else if (fieldFormatType.equals("Integer")) {
                    f = new IntegerField(fieldContents[i]);
                }else if(fieldFormatType.equals("Date")){
                   //TODO
                } else if(fieldFormatType.equals("Time")){

                }else if(fieldFormatType.equals("DateTime")){

                }
                fieldList.add(f);
            }
            return fieldList;
        }

        return null;
    }
}
