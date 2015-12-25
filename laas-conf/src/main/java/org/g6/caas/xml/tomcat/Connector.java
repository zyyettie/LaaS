package org.g6.caas.xml.tomcat;

import lombok.Data;

@Data
public class Connector {
    private String port;
    private String protocol;
    private String connectionTimeout;
    private String redirectPort;
    private String executor;
    //for SSL only
    private boolean SSLEnabled;
    private boolean clientAuth;
    private String scheme;
    private boolean secure;
    private String sslProtocol;
    private String keystoreFile;
    private String keystorePass;
    private String truststoreFile;
    private String truststorePass;

    private String maxThreads;

     //For AJP/1.3 only
    private boolean tomcatAuthentication;
    private boolean tomcatAuthorization;

}
