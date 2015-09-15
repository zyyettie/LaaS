package org.g6.laas.core.engine.task.report.template;

import java.io.File;

public class FileTemplateLoader implements TemplateLoader{

    private File rootDir;
    private String fileSuffix;

    public FileTemplateLoader(String rootDir, String fileSuffix){
        this.rootDir = new File(rootDir);
        this.fileSuffix = fileSuffix;
    }

    public File loadTemplate(String name){
        File[] templates = rootDir.listFiles();
        for(File file : templates){
            if(file.getName().equals(name+fileSuffix))
                return file;
        }
        return null;
    }
}
