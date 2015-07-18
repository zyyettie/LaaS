package org.g6.laas.core.format;

import lombok.Data;
import org.g6.laas.core.format.cache.FileFormatCache;
import org.g6.laas.core.log.line.LineAttributes;
import org.g6.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Data
@Service
public class FileFormatCacheService {
    @Autowired
    private FileFormatCache cache;
    private String formatKey;
    private boolean regex;

    public FileFormatCacheService(String formatKey) {
        this.formatKey = formatKey;
    }

    /**
     * Get the key that is used to identify one line per name.
     * Note there may be thread safe issue if this class is initialized by Spring
     * with singleton mode
     *
     * @param name
     * @return
     */
    public String getLineKeyBy(String name) {
        LineAttributes lineAttr = getLineAttributesBy(name);
        String key = lineAttr.getKey();
        if (key.startsWith(Constants.REGEX_PREFIX)) {
            key = key.substring(Constants.REGEX_PREFIX.length());
            regex = true;
        }
        return key;
    }

    public LineAttributes getLineAttributesBy(String name) {
        List<LineAttributes> lineFormats = cache.getFileFormat(formatKey);
        for (LineAttributes lineAttr : lineFormats) {
            if (lineAttr.getName().equals(name)) {
                return lineAttr;
            }
        }

        return null;
    }
}
