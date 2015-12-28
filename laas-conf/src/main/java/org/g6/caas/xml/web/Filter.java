package org.g6.caas.xml.web;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Filter {
    private String icon;
    private String name;
    private String displayName;
    private String description;
    private String filterClass;
    private List<InitParam> initParams;

    public void addInitParam(InitParam param) {
        if (initParams == null)
            initParams = new ArrayList<>();
        initParams.add(param);
    }
}
