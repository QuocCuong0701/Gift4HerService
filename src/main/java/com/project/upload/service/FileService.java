package com.project.upload.service;

import com.google.api.client.util.Lists;
import com.google.api.gax.paging.Page;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.project.upload.constant.Constants;
import com.project.upload.entity.FileEntity;
import com.project.upload.repository.FileRepository;
import com.project.upload.utils.HttpUtil;
import com.project.upload.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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
import java.util.stream.Collectors;

@Service
public class FileService {

    @Autowired
    UploadService uploadService;
    @Autowired
    FileRepository fileRepository;

    /**
     * get all files
     */
    public List<FileEntity> getAllFiles() {
        return fileRepository.findAllFiles();
    }

    /**
     * Upload file function
     */
    public Object upload(MultipartFile[] multipartFiles) {
        List<FileEntity> fileEntities = new ArrayList<>();
        Arrays.asList(multipartFiles).forEach(multipartFile -> {
            String fileName = multipartFile.getOriginalFilename();
            fileName = UUID.randomUUID().toString().concat(Utils.getExtension(fileName));
            String category = Utils.getFileType(fileName);

            File file = null;
            String fileNameCrop = null;
            Blob uploadBlob = null;
            Long size = null;
            try {
                // Upload original image
                file = uploadService.convertToFile(multipartFile, fileName);
                size = file.length() / 1024;
                uploadBlob = uploadService.uploadFile(file, fileName);
                file.delete();
                // Upload cropped image
                BufferedImage image = Utils.cropImageSquare(multipartFile.getBytes());
                fileNameCrop = Utils.generateFileNameCrop(fileName);
                file = new File(fileNameCrop);
                ImageIO.write(image, category, file);
                uploadService.uploadFile(file, fileNameCrop);
                file.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String fileUriOri = String.format(UploadService.DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            String fileUriCrop = String.format(UploadService.DOWNLOAD_URL, URLEncoder.encode(fileNameCrop, StandardCharsets.UTF_8));
            Date createdDate = Utils.millisecondToDate(uploadBlob.getCreateTimeOffsetDateTime().toEpochSecond());

            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(fileName);
            fileEntity.setCategory(category);
            fileEntity.setCreatedDate(createdDate);
            fileEntity.setFileUriOriginal(fileUriOri);
            fileEntity.setFileUriCrop(fileUriCrop);
            fileEntity.setSize(size);
            fileEntity.setStatus(Constants.FirebaseStatus.EXIST);

            fileEntities.add(fileRepository.save(fileEntity));
        });

        return fileEntities;
    }

    /**
     * Download file function
     */
    public void download(String fileName) {
        String destFileName = UUID.randomUUID().toString().concat(Utils.getExtension(fileName));
        String destFilePath = System.getProperty("user.home") + "/Downloads/" + destFileName;

        Storage storage = getStorage();
        Blob blob = storage.get(BlobId.of(UploadService.BUCKET_NAME, fileName));
        blob.downloadTo(Paths.get(destFilePath));
    }

    /**
     * Update file status if file doesn't exist on firebase anymore
     */
    public void updateIfFileNotExist() {
        List<FileEntity> fileEntities = fileRepository.findAll();
        for (FileEntity fe : fileEntities) {
            int codeOri = HttpUtil.getStatusCode(fe.getFileUriOriginal());
            int codeCrop = HttpUtil.getStatusCode(fe.getFileUriCrop());
            if ((codeOri != HttpStatus.OK.value() || codeCrop != HttpStatus.OK.value())
                    && fe.getStatus() == Constants.FirebaseStatus.EXIST) {
                fileRepository.updateFileStatusNotExist(fe.getId());
            }
        }
    }

    public List<FileEntity> getAllFromStorage() {
        Storage storage = getStorage();
        Page<Blob> blobs = storage.list(UploadService.BUCKET_NAME);
        List<Blob> blobList = Lists.newArrayList();
        blobs.iterateAll().forEach(blobList::add);

        List<Blob> blobOriginals = blobList.stream()
                .filter(blob -> !blob.getBlobId().getName().contains("_crop"))
                .collect(Collectors.toList());

        List<FileEntity> fileEntities = Lists.newArrayList();
        blobOriginals.forEach(blob -> fileEntities.add(buildFileEntity(blob)));

        fileRepository.saveAll(fileEntities);
        return fileEntities;
    }

    private Storage getStorage() {
        try {
            Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(UploadService.PRIVATE_KEY_JSON_PATH));
            return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String buildLink(String fileName) {
        return Constants.LINK + UploadService.BUCKET_NAME + "/o/" + fileName + Constants.ALT_MEDIA;
    }

    private FileEntity buildFileEntity(Blob blob) {
        String fileName = blob.getBlobId().getName();
        return FileEntity.builder()
                .fileName(fileName)
                .category(blob.getContentType())
                .createdDate(Utils.millisecondToDate(blob.getTimeStorageClassUpdatedOffsetDateTime().toEpochSecond()))
                .fileUriOriginal(buildLink(fileName))
                .fileUriCrop(buildLink(Utils.generateFileNameCrop(fileName)))
                .size(blob.getSize())
                .status(Constants.FirebaseStatus.EXIST)
                .build();
    }
}
