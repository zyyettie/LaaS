package org.g6.laas.server.controllers;

import org.g6.laas.server.database.entity.File;
import org.g6.laas.server.database.entity.user.User;
import org.g6.laas.server.database.repository.IFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FileController {
    @Autowired
    private IFileRepository fileRep;

    @RequestMapping(value = "/controllers/files")
    ResponseEntity<String> deleteFiles(@RequestBody List<File> files){
        for(File file : files){
            File tempFile = fileRep.findOne(file.getId());
            tempFile.setIsRemoved("Y");
            fileRep.save(tempFile);
        }
        return new ResponseEntity("{\"id\":1}", HttpStatus.OK);
    }
}
