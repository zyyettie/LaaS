package org.g6.laas.server.controllers;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import org.g6.laas.server.database.entity.file.FileType;
import org.g6.laas.server.database.entity.user.Quota;
import org.g6.laas.server.database.repository.IFileRepository;
import org.g6.laas.server.database.repository.IFileTypeRepository;
import org.g6.laas.server.database.repository.IQuotaRepository;
import org.g6.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class FileUploadController {

    @Autowired
    private ServletContext context;

    @Autowired
    private IFileRepository fileRepository;
    @Autowired
    private IFileTypeRepository fileTypeRep;

    @Autowired
    private IQuotaRepository quotaRepository;

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
            @RequestParam("files[]") MultipartFile[] files,
            @RequestParam("fileType") String fileTypeId,
            @RequestParam("desc") String desc,
            HttpServletRequest request) {
        String uploadedPath = context.getRealPath("/upload");
        Collection<UploadResult> results = new ArrayList<>();
        for (MultipartFile file : files) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            String todayFolder = dateFormatter.format(new Date());
            String fileName = file.getOriginalFilename();
            try {
                String generatedName = UUID.randomUUID().toString();
                String path = uploadedPath + "/" + todayFolder + "/";
                String fullFileName = path+ generatedName;
                File uploaded = new File(fullFileName);
                Files.createParentDirs(uploaded);
                long size = file.getSize();
                InputStream inputStream = file.getInputStream();
                OutputStream outputStream = new FileOutputStream(fullFileName);
                ByteStreams.copy(inputStream, outputStream);
                inputStream.close();
                outputStream.close();

                org.g6.laas.server.database.entity.file.File fileEntity = new org.g6.laas.server.database.entity.file.File();
                fileEntity.setOriginalName(fileName);
                fileEntity.setFileName(generatedName);
                fileEntity.setPath(path);
                fileEntity.setSize(size);
                fileEntity.setDescription(desc);
                fileEntity.setIsRemoved("N");
                FileType type = fileTypeRep.findOne(new Long(fileTypeId));
                fileEntity.setType(type);

                org.g6.laas.server.database.entity.file.File saved = fileRepository.save(fileEntity);

                Quota quota = quotaRepository.findUserQuota(saved.getCreatedBy().getName());
                quota.setUsedSpace(quota.getUsedSpace() + size);
                Quota updatedQuota = quotaRepository.save(quota);

                results.add(new UploadResult(saved.getId(), fileName, size, "succeed"));
            } catch (IOException e) {
                results.add(new UploadResult(-1L, fileName, -1L, "failed"));
            }

        }
        return results;
    }

    @RequestMapping(value = "/download/{fileName}")
    public String handleFileUpload(@PathVariable String fileName, HttpServletRequest request,
                                 HttpServletResponse response){
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName="
                + fileName);
        try {
            String path = FileUtil.getvalue("result_file_full_path", "sm.properties");
            InputStream inputStream = new FileInputStream(new File(path
                    + File.separator + fileName));

            OutputStream os = response.getOutputStream();
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
            os.close();

            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //  返回值要注意，要不然就出现下面这句错误！
        //java+getOutputStream() has already been called for this response
        return null;

    }

}
