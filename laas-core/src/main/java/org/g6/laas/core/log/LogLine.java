package org.g6.laas.core.log;

import lombok.NoArgsConstructor;
import org.g6.laas.core.exception.InputFormatNotFoundException;
import org.g6.laas.core.exception.LaaSRuntimeException;
import org.g6.laas.core.exception.Regex4LineSplitNotFoundException;
import org.g6.laas.core.field.DoubleField;
import org.g6.laas.core.field.Field;
import org.g6.laas.core.field.IntegerField;
import org.g6.laas.core.field.TextField;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.format.FieldFormat;
import org.g6.laas.core.format.InputFormat;
import org.g6.util.RegexUtil;

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
     * Whether split depends on the search result. From the code design, if InputFormat comes
     * we think each line in the return result should be split according to matching format.
     * <p/>
     * But there are some limitations. one is all lines must have matching formats, otherwise
     * the InputFormatNotFoundException is thrown.
     * <p/>
     * The specified InputFormat can not cover all the ones. so the split method can be overwritten
     * and no matter whether the InputFormat is needed or not, because the code will run on the line
     * content directly.
     *
     * @return
     */
    @Override
    public Collection<Field> split() {
        if (isSplitNeeded()) {
            String lineSplitRegex = null;
            List<FieldFormat> fieldFormatList = null;
            List<String> errorKeyList = new ArrayList<>();
            int counter = 0;
            //here if only split with separator, the line format should not be special
            Map<String, List<FieldFormat>> formats = getInputFormat().getLineFormatsByKey();
            for (Map.Entry<String, List<FieldFormat>> entry : formats.entrySet()) {
                if (getContent().contains(entry.getKey())) {
                    errorKeyList.add(entry.getKey());
                    counter++;
                    fieldFormatList = entry.getValue();
                    lineSplitRegex = getInputFormat().getRegex4LineSplit().get(entry.getKey());
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
            if (lineSplitRegex == null)
                throw new Regex4LineSplitNotFoundException("Regex not found for " + getContent());

            String[] fieldContents = RegexUtil.getValues(getContent(), lineSplitRegex);
            Collection<Field> fieldList = new ArrayList<>();

            for (int i = 0; i < fieldContents.length; i++) {
                Field f = null;
                FieldFormat ff = fieldFormatList.get(i);
                String fieldFormatType = ff.getType();

                if (fieldFormatType.equals("String")) {
                    f = new TextField(fieldContents[i]);
                    if (ff.isSortable()) {
                        this.sortValue = fieldContents[i];
                    }
                } else if (fieldFormatType.equals("Integer")) {
                    f = new IntegerField(fieldContents[i]);
                } else if (fieldFormatType.equals("DateTime")) {
                  //DODO
                } else if (fieldFormatType.equals("Double")) {
                    f = new DoubleField(fieldContents[i]);
                }
                fieldList.add(f);
            }
            return fieldList;
        }

        return null;
    }

    /**
     * Here should be a limitation that only Double and Date type
     * can be compared. in split method, no matter what field format type should be
     * sortValue always must be either Double and Date.
     *
     * @param o
     * @return
     */
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
