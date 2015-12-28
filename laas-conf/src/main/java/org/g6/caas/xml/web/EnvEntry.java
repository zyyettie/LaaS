package org.g6.caas.xml.web;

import lombok.Data;

@Data
public class EnvEntry {
    private String description;
    private String name;
    private String value;
    private String type;
}
