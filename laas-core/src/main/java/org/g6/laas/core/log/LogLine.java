package org.g6.laas.core.log;

import lombok.NoArgsConstructor;
import org.g6.laas.core.exception.InputFormatNotFoundException;
import org.g6.laas.core.exception.LaaSRuntimeException;
import org.g6.laas.core.exception.Regex4LineSplitNotFoundException;
import org.g6.laas.core.field.*;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.format.FieldFormat;
import org.g6.laas.core.format.InputFormat;
import org.g6.util.Constants;
import org.g6.util.RegexUtil;
import org.g6.util.StringUtil;

import java.util.*;

@NoArgsConstructor
public class LogLine extends Line {
    Field sortedField = null;

    public LogLine(ILogFile file, String content, int lineNumber) {
        super(file, content, lineNumber, null);
    }

    public LogLine(ILogFile file, String content, int lineNumber, InputFormat lineFormat) {
        super(file, content, lineNumber, lineFormat);
    }

    /**
     * The specified InputFormat can not cover all the ones. so the split method can be overwritten
     * and no matter whether the InputFormat is needed or not, because the code will run on the line
     * content directly.
     *
     * @return
     */
    @Override
    public SplitResult split() {
        String lineSplitRegex = null;
        List<FieldFormat> fieldFormatList = null;
        List<String> errorKeyList = new ArrayList<>();
        int counter = 0;

        Map<String, List<FieldFormat>> formats = getInputFormat().getLineFormatsByKey();
        for (Map.Entry<String, List<FieldFormat>> entry : formats.entrySet()) {
            String lineFormatKey = entry.getKey();

            if (lineFormatKey.startsWith(Constants.REGEX_PREFIX)) {// the key is regex
                String regex = lineFormatKey.substring(Constants.REGEX_PREFIX.length());
                String matchedValue = RegexUtil.getValue(getContent(), regex);

                if(!StringUtil.isNull(matchedValue)){
                    errorKeyList.add(lineFormatKey);
                    counter++;
                    fieldFormatList = entry.getValue();
                    lineSplitRegex = getInputFormat().getRegex4LineSplit().get(lineFormatKey);
                }
            } else {
                if (getContent().contains(lineFormatKey)) {
                    errorKeyList.add(entry.getKey());
                    counter++;
                    fieldFormatList = entry.getValue();
                    lineSplitRegex = getInputFormat().getRegex4LineSplit().get(lineFormatKey);
                }
            }
        }

        if (counter != 1) {
            StringBuffer sb = new StringBuffer();
            sb.append("Found more than one formats for the line which number is ")
                    .append(getLineNumber())
                    .append(" in ")
                    .append(getFile().getName())
                    .append(". The key words matched are: ");
            int keyCount = 1;
            for (String key : errorKeyList) {
                sb.append(key);
                if (errorKeyList.size() != keyCount) {
                    sb.append(", ");
                }
                keyCount++;
            }
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

            if (fieldFormatType.equals(Constants.FIELD_FORMAT_TYPE_STRING)) {
                f = new TextField(fieldContents[i]);
            } else if (fieldFormatType.equals(Constants.FIELD_FORMAT_TYPE_INTEGER)) {
                f = new IntegerField(fieldContents[i]);
            } else if (fieldFormatType.equals(Constants.FIELD_FORMAT_TYPE_DATETIME)) {
                f = new DateTimeField(fieldContents[i], ff.getDateFormat());
            } else if (fieldFormatType.equals(Constants.FIELD_FORMAT_TYPE_DOUBLE)) {
                f = new DoubleField(fieldContents[i]);
            }

            if (ff.isSortable()) {
                sortedField = f;
            }
            fieldList.add(f);
        }
        return new BasicSplitResult(fieldList);
    }

    @Override
    public int compareTo(Object o) {
        return sortedField.compareTo(((LogLine) o).sortedField);
    }

}
