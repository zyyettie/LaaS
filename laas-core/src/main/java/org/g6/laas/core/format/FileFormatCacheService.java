package org.g6.laas.core.format;

import lombok.Data;
import org.g6.laas.core.format.cache.FileFormatCache;
import org.g6.laas.core.log.line.LineAttributes;
import org.g6.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Service
public class FileFormatCacheService {
    @Autowired
    private FileFormatCache cache = new FileFormatCache();
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

    /**
     * Get the collection of Line format per line name
     *
     * @param names the line names defined in JSON file
     * @return
     */
    public List<LineAttributes> getLineAttrListBy(String[] names) {
        List<LineAttributes> lineFormats = cache.getFileFormat(formatKey);
        List<LineAttributes> list = new ArrayList<>();

        for (LineAttributes lineAttr : lineFormats) {
            String name = lineAttr.getName();

            for(String lineName : names){
                if(lineName.equals(name)){
                    list.add(lineAttr);
                }
            }
        }

        return list;
    }

    public List<LineAttributes> getAllLineAttrList(){
        return cache.getFileFormat(formatKey);
    }
}
