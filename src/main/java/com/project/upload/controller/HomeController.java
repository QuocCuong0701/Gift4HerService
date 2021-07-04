package com.project.upload.controller;

import com.project.upload.entity.FileEntity;
import com.project.upload.service.FileService;
import com.project.upload.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class HomeController {

    @Autowired
    FileService fileService;

    @GetMapping("/")
    public Object getAllFile() {
        try {
            List<FileEntity> fileEntities = fileService.getAllFiles();
            return Utils.responseData(HttpStatus.OK, true, "Get Data Successfully.", fileEntities);
        } catch (Exception e) {
            return Utils.responseData(HttpStatus.INTERNAL_SERVER_ERROR, false, "Get Data Unsuccessfully.", e);
        }
    }
}
