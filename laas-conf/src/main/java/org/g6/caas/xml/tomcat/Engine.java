package org.g6.caas.xml.tomcat;

import lombok.Data;

@Data
public class Engine {
    private String name;
    private String defaultHost;
    private String jvmRoute;
    private String className;
    private String startStopThreads;
}
