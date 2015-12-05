package org.g6.laas.sm.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SMRTEInfo implements Serializable {
    private LoginInfo loginInfo;

    private String serverVersion;
    private String appsVersion;
    private String dbName;
    private String dbCase;
    private boolean FIPs;
    private boolean TSO;
    private boolean LWSSO;
    private boolean CAC;
    private String webServer;
    private boolean debugdbquery;
    private boolean rtm3;
    private boolean debughttp;
}
