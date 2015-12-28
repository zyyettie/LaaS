package org.g6.caas.xml.tomcat;

import lombok.Data;

import java.util.List;

@Data
public class Server {
    private String port;
    private String shutDown;
    private String address="localhost";
    private String className;

    List<Listener> listeners;
    Service service;
}
