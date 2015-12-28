package org.g6.caas.xml.tomcat;

import lombok.Data;

@Data
public class Value {
    private String className;
    private String directory;
    private String prefix;
    private String suffix;
    private String pattern;
}
