package org.g6.laas.sm.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginInfo implements Serializable {
    private String licenseType;
    private String clientType;
    private String webServer;
    private String clientVersion;
    private boolean TSO = false;
    private boolean CAC = false;
}
