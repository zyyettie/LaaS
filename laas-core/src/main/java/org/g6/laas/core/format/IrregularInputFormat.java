package org.g6.laas.core.format;

import lombok.Data;
import org.g6.laas.core.log.LineAttributes;

import java.util.List;
import java.util.Map;

@Data
public class IrregularInputFormat implements InputFormat<Map<String, LineAttributes>>{
    Map<String, LineAttributes> lineFormat;

    @Override
    public Map<String, LineAttributes> getLineFormat() {
        return lineFormat;
    }
}
