package org.g6.laas.core.engine.task.report.template;

import java.io.File;
import java.io.FileNotFoundException;

public class TemplateLoader {

    private File rootDir;
    private String fileSuffix;

    public TemplateLoader(String rootDir,String fileSuffix){
        this.rootDir = new File(rootDir);
        this.fileSuffix = fileSuffix;
    }

    public File loadTemplate(String name) throws FileNotFoundException{
        File[] templates = rootDir.listFiles();
        for(File file : templates){
            if(file.getName().equals(name+fileSuffix))
                return file;
        }
        throw new FileNotFoundException(name+fileSuffix);
    }

}
