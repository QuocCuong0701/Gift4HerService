package com.project.upload.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.project.upload.utils.Utils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

@Component
public class UploadService {

    static final String BUCKET_NAME = "upload-file-fb.appspot.com";
    static final String PRIVATE_KEY_JSON_PATH = "upload-file.json";
    static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/" + BUCKET_NAME + "/o/%s?alt=media";

    /**
     * Upload to Firebase
     *
     * @param file
     * @param fileName
     * @return
     * @throws IOException
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
