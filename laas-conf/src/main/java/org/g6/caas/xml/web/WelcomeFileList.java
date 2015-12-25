package org.g6.caas.xml.web;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WelcomeFileList {
    private List<WelcomeFile> fileList;

    public void addWelcomeFile(WelcomeFile file){
        if(fileList == null)
            fileList = new ArrayList<>();

        fileList.add(file);
    }
}
