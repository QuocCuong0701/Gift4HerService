package com.project.upload.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.project.upload.entity.FileEntity;
import com.project.upload.repository.FileRepository;
import com.project.upload.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;

@Component
public class FileService {

    @Autowired
    UploadService uploadService;
    @Autowired
    FileRepository fileRepository;

    /**
     * get all files
     *
     * @return
     */
    public List<FileEntity> getAllFiles() {
        return fileRepository.findAll();
    }

    /**
     * Upload file function
     *
     * @param multipartFiles
     * @return
     */
    public Object upload(MultipartFile[] multipartFiles) {
        List<FileEntity> fileEntities = new ArrayList<>();
        Arrays.asList(multipartFiles).stream().forEach(multipartFile -> {
            String fileName = multipartFile.getOriginalFilename();
            fileName = UUID.randomUUID().toString().concat(Utils.getExtension(fileName));
            String category = Utils.getFileType(fileName);

            File file = null;
            String fileNameCrop = null;
            Blob uploadBlob = null;
            try {
                // Upload original image
                file = uploadService.convertToFile(multipartFile, fileName);
                uploadBlob = uploadService.uploadFile(file, fileName);
                file.delete();
                // Upload cropped image
                BufferedImage bi = Utils.cropImageSquare(multipartFile.getBytes());
                fileNameCrop = Utils.generateFileNameCrop(fileName);
                file = new File(fileNameCrop);
                ImageIO.write(bi, category, file);
                uploadService.uploadFile(file, fileNameCrop);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String fileUriOri = String.format(UploadService.DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            String fileUriCrop = String.format(UploadService.DOWNLOAD_URL, URLEncoder.encode(fileNameCrop, StandardCharsets.UTF_8));
            Date createdDate = Utils.millisecondToDate(uploadBlob.getCreateTime());

            file.delete();

            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(fileName);
            fileEntity.setCategory(category);
            fileEntity.setCreatedDate(createdDate);
            fileEntity.setFileUriOriginal(fileUriOri);
            fileEntity.setFileUriCrop(fileUriCrop);

            fileEntities.add(fileRepository.save(fileEntity));
        });

        return fileEntities;
    }

    /**
     * Download file function
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public Object download(String fileName) throws IOException {
        try {
            String destFileName = UUID.randomUUID().toString().concat(Utils.getExtension(fileName));
            String destFilePath = "F:/IdeaProjects/upload/upload_file/" + destFileName;

            Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(UploadService.PRIVATE_KEY_JSON_PATH));
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            Blob blob = storage.get(BlobId.of(UploadService.BUCKET_NAME, fileName));
            blob.downloadTo(Paths.get(destFilePath));
            return blob;
//            return Utils.responseData(HttpStatus.OK, true, "Successfully Downloaded.", blob.getMediaLink());
        } catch (Exception e) {
            return e;
        }
    }
}
