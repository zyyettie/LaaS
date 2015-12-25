package org.g6.caas.xml.tomcat;

import lombok.Data;

@Data
public class Listener {
    private String className;
    private String SSLEngine = "on";
    private String SSLRandomSeed = "builtin";
    private String FIPSMode = "off";
}
