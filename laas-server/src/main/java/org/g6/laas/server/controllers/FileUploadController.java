package org.g6.laas.server.controllers;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import org.g6.laas.server.database.repository.IFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class FileUploadController {

    private static final String uploadedPath = "/uploaded";

    @Autowired
    private IFileRepository fileRepository;

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public
    @ResponseBody
    String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public
    @ResponseBody
    Collection<UploadResult> handleFileUpload(
            @RequestParam("files") MultipartFile[] files) {
        Collection<UploadResult> results = new ArrayList<>();
        for (MultipartFile file : files) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            String todayFolder = dateFormatter.format(Calendar.getInstance());
            String fileName = file.getOriginalFilename();
            try {
                InputStream inputSteam = file.getInputStream();
                String generatedName = UUID.randomUUID().toString();
                String path = uploadedPath + "/" + todayFolder + "/" + generatedName;
                File uploaded = new File(path);
                Files.createParentDirs(uploaded);
                OutputStream outputStream = new FileOutputStream(uploaded);
                long size = ByteStreams.copy(inputSteam, outputStream);
                outputStream.flush();
                outputStream.close();
                inputSteam.close();
                org.g6.laas.server.database.entity.File fileEntity = new org.g6.laas.server.database.entity.File();
                fileEntity.setOrigialName(fileName);
                fileEntity.setFileName(generatedName);
                fileEntity.setPath(uploaded.getCanonicalPath());
                fileEntity.setSize(size);
                org.g6.laas.server.database.entity.File saved = fileRepository.save(fileEntity);
                results.add(new UploadResult(saved.getId(),fileName, size, "succeed"));
            } catch (IOException e) {
                results.add(new UploadResult(-1L,fileName, -1L, "failed"));
            }

        }
        return results;
    }

}
