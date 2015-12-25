package org.g6.caas.xml.web;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ServletMapping {
    private String name;
    private List<UrlPattern> urlPatterns;

    public void addUrlPattern(UrlPattern pattern){
        if(urlPatterns == null)
            urlPatterns = new ArrayList<>();
        urlPatterns.add(pattern);
    }
}
