package org.g6.caas.xml.web;

import lombok.Data;

@Data
public class WebResourceCollection {
    private String name;
    private String description;
    private String urlPattern;
    private String httpMethod;
}
