package com.project.upload.controller;

import com.google.cloud.storage.Blob;
import com.project.upload.entity.FileEntity;
import com.project.upload.service.FileService;
import com.project.upload.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/file")
public class UploadController {

    @Autowired
    FileService fileService;

    @PostMapping("/upload")
    public Object upload(@RequestParam("file") MultipartFile[] multipartFile) {
        try {
            List<FileEntity> fileEntities = (List<FileEntity>) fileService.upload(multipartFile);
            return Utils.responseData(HttpStatus.OK, true, "Successfully Uploaded.", fileEntities);
        } catch (Exception e) {
            return Utils.responseData(HttpStatus.INTERNAL_SERVER_ERROR, false, "Unsuccessfully Uploaded.", e);
        }
    }

    @PostMapping("/download/{fileName}")
    public Object download(@PathVariable String fileName) throws IOException {
        try {
            Blob blob = (Blob) fileService.download(fileName);
            return Utils.responseData(HttpStatus.OK, true, "Successfully Downloaded.", blob);
        } catch (Exception e) {
            return Utils.responseData(HttpStatus.INTERNAL_SERVER_ERROR, false, "Unsuccessfully Downloaded.", e);
        }

    }
}
