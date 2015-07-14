package org.g6.laas.core.format;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.g6.laas.core.exception.InputFormatNotFoundException;
import org.g6.laas.core.exception.LaaSCoreRuntimeException;
import org.g6.laas.core.exception.Regex4LineSplitNotFoundException;
import org.g6.laas.core.field.*;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.format.cache.InputFormatCache;
import org.g6.laas.core.log.*;
import org.g6.util.Constants;
import org.g6.util.RegexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public final class DefaultInputFormat implements InputFormat {
    @Autowired
    InputFormatCache cache;
    private ILogFile file;

    public DefaultInputFormat(ILogFile file) {
        this.file = file;
    }

    @Override
    public SplitResult getSplits(Line line) {
        //cache object should be injected by Spring
        if(cache == null)
            cache = new InputFormatCache();
        Map<String, LineAttributes> lineAttrMap =cache.getAllInputFormats().get(file.getFormatKey());
        String lineSplitRegex = null;
        List<FieldFormat> fieldFormatList = null;
        List<String> errorKeyList = new ArrayList<>();
        int counter = 0;

        for (Map.Entry<String, LineAttributes> entry : lineAttrMap.entrySet()) {
            String lineFormatKey = entry.getKey();
            LineAttributes lineAttr = entry.getValue();

            if (lineFormatKey.startsWith(Constants.REGEX_PREFIX)) {// the key is regex
                String regex = lineFormatKey.substring(Constants.REGEX_PREFIX.length());
                String matchedValue = RegexUtil.getValue(line.getContent(), regex);

                if (!StringUtils.isBlank(matchedValue)) {
                    errorKeyList.add(lineFormatKey);
                    counter++;
                    fieldFormatList = lineAttr.getFieldFormats();
                    lineSplitRegex = lineAttr.getSplitRegex();
                }
            } else {
                if (line.getContent().contains(lineFormatKey)) {
                    errorKeyList.add(entry.getKey());
                    counter++;
                    fieldFormatList = lineAttr.getFieldFormats();
                    lineSplitRegex = lineAttr.getSplitRegex();
                }
            }
        }

        if (counter != 1) {
            StringBuffer sb = new StringBuffer();
            sb.append("Found more than one formats for the line which number is ")
                    .append(line.getLineNumber())
                    .append(" in ")
                    .append(line.getFile().getName())
                    .append(". The key words matched are: ");
            int keyCount = 1;
            for (String key : errorKeyList) {
                sb.append(key);
                if (errorKeyList.size() != keyCount) {
                    sb.append(", ");
                }
                keyCount++;
            }
            throw new LaaSCoreRuntimeException(sb.toString());
        }
        if (fieldFormatList == null)
            throw new InputFormatNotFoundException("InputFormat not found");
        if (lineSplitRegex == null)
            throw new Regex4LineSplitNotFoundException("Regex not found for " + line.getContent());
        String test = line.getContent();
        String[] fieldContents = RegexUtil.getValues(line.getContent(), lineSplitRegex);
        Collection<Field> fieldList = new ArrayList<>();

        for (int i = 0; i < fieldContents.length; i++) {
            Field f = null;
            FieldFormat ff = fieldFormatList.get(i);
            String fieldFormatType = ff.getType();

            if (fieldFormatType.equals(Constants.FIELD_FORMAT_TYPE_STRING)) {
                f = new TextField(ff.getName(), fieldContents[i]);
            } else if (fieldFormatType.equals(Constants.FIELD_FORMAT_TYPE_INTEGER)) {
                f = new IntegerField(ff.getName(), fieldContents[i]);
            } else if (fieldFormatType.equals(Constants.FIELD_FORMAT_TYPE_DATETIME)) {
                f = new DateTimeField(ff.getName(), fieldContents[i], ff.getDateFormat());
            } else if (fieldFormatType.equals(Constants.FIELD_FORMAT_TYPE_DOUBLE)) {
                f = new DoubleField(ff.getName(), fieldContents[i]);
            }

            if (ff.isSortable() && (line instanceof LogLine)) {

                ((LogLine) line).setSortedField(f);
            }
            fieldList.add(f);
        }
        return new BasicSplitResult(fieldList);
    }

}
