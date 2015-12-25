package org.g6.caas.xml.tomcat;

import lombok.Data;

@Data
public class Host {
    private String name;
    private String appBase;
    private boolean unpackWARs;
    private boolean autoDeploy;
}
