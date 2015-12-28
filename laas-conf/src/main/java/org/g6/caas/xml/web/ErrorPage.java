package org.g6.caas.xml.web;

import lombok.Data;

@Data
public class ErrorPage {
    private String code;
    private String exceptionType;
    private String location;
}
