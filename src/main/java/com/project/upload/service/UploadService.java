package com.project.upload.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.project.upload.constant.Constants;
import com.project.upload.utils.Utils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class UploadService {

    static final String BUCKET_NAME = "photosgallery4her.appspot.com";
    static final String PRIVATE_KEY_JSON_PATH = "upload-file.json";
    static final String DOWNLOAD_URL = Constants.LINK + BUCKET_NAME + "/o/%s" + Constants.ALT_MEDIA;

    /**
     * Upload to Firebase
     */
    Blob uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/" + Utils.getFileType(fileName)).build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(PRIVATE_KEY_JSON_PATH));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        return storage.create(blobInfo, Files.readAllBytes(file.toPath()));
    }

    File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }

}
