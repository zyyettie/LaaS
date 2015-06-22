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

import java.util.*;

@NoArgsConstructor
public class LogLine extends Line {
    Object sortValue;

    public LogLine(ILogFile file, String content, int lineNumber) {
        super(file, content, lineNumber, null);
    }

    public LogLine(ILogFile file, String content, int lineNumber, InputFormat lineFormat) {
        super(file, content, lineNumber, lineFormat);
        split();
    }

    /**
     * Consider how to split this line from SM RTE log file:
     * 9584(  9124) 06/20/2015 17:05:22  RTE D DBQUERY^F^triggers(sqlserver I)^0^0.000000^ ^13^0.016000^"table.name="cm3t""^{"table.name", "trigger.type"}^0.000000^0.000000 ( [ 1] display show.rio )
     *
     * @return
     */
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
                    //assume you split the column just because you want to get sortable value
                    if (ff.isSortable() && ff.isSplitable()) {
                        String[] secSplitValues = fieldContents[i].split(ff.getSeparator());
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
        if (sortValue instanceof Double) {
            Double d1 = (Double) this.sortValue;
            Double d2 = (Double) ((LogLine) o).sortValue;

            return (-1) * d1.compareTo(d2); //sort by desc
        } else if (sortValue instanceof String) {
            //TODO
            //need to check what is the real type e.g. double, date etc.
        }
        return 0;
    }

    public Object getSortValue() {
        return sortValue;
    }

}
