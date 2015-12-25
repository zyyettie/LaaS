package org.g6.caas.xml.web;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class JspConfig {
    private List<Taglib> taglibs;
    private JspPropertyGroup jspPropertyGroup;

    public void addTaglib(Taglib tag) {
        if (taglibs == null)
            taglibs = new ArrayList<>();

        taglibs.add(tag);
    }
}
