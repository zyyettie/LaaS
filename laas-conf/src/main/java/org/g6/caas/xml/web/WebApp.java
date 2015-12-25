package org.g6.caas.xml.web;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WebApp {
    private String version;
    private String metadataComplete;
    private String xmlns;
    private String xmlns_xsi;
    private String xsi_schemaLocation;

    private String displayName;
    private String description;

    private List<ContextParam> contextParams;
    private List<MimeMapping> mimeMappings;
    private List<Filter> filters;
    private List<FilterMapping> filterMappings;
    private List<Listener> listeners;
    private List<Servlet> servlets;
    private List<ServletMapping> servletMappings;
    private List<ErrorPage> errorPages;

    private SessionConfig sessionConfig;
    private WelcomeFileList welcomeFileList;
    private JspConfig jspConfig;
    private EnvEntry envEntry;

    public void addContextParam(ContextParam param){
        if(contextParams == null)
            contextParams = new ArrayList<>();

        contextParams.add(param);
    }

    public void addMimeMapping(MimeMapping mimeMapping){
        if(mimeMappings == null)
            mimeMappings = new ArrayList<>();

        mimeMappings.add(mimeMapping);
    }

    public void addFilter(Filter filter){
        if(filters == null)
            filters = new ArrayList<>();

        filters.add(filter);
    }

    public void addFilterMapping(FilterMapping filterMapping){
        if(filterMappings == null)
            filterMappings = new ArrayList<>();

        filterMappings.add(filterMapping);
    }
    public void addListener(Listener listener){
        if(listeners == null)
            listeners = new ArrayList<>();

        listeners.add(listener);
    }
    public void addServlet(Servlet servlet){
        if(servlets == null)
            servlets = new ArrayList<>();

        servlets.add(servlet);
    }
    public void addServletMapping(ServletMapping servletMapping){
        if(servletMappings == null)
            servletMappings = new ArrayList<>();

        servletMappings.add(servletMapping);
    }
    public void addErrorPage(ErrorPage errorPage){
        if(errorPages == null)
            errorPages = new ArrayList<>();

        errorPages.add(errorPage);
    }
}
