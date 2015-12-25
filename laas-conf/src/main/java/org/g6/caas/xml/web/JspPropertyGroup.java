package org.g6.caas.xml.web;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class JspPropertyGroup {
    private List<UrlPattern> urlPatternList;
    private boolean elIgnored;
    private boolean scriptingInvalid;
    private String pageEncoding;
    private String includePrelude;
    private String includeCoda;


    public void addUrlPattern(UrlPattern urlPattern){
        if(urlPatternList == null)
            urlPatternList = new ArrayList<>();

        urlPatternList.add(urlPattern);
    }
}
