package com.project.upload.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "FILE")
public class FileEntity {
    private Long id;
    private String fileName;
    private String fileUriOriginal;
    private String fileUriCrop;
    private Double size;
    private int status;
    private String category;
    private Date createdDate;

    public FileEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "FILE_NAME")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column(name = "FILE_URI_ORIGINAL")
    public String getFileUriOriginal() {
        return fileUriOriginal;
    }

    public void setFileUriOriginal(String fileUriOriginal) {
        this.fileUriOriginal = fileUriOriginal;
    }

    @Column(name = "FILE_URI_CROP")
    public String getFileUriCrop() {
        return fileUriCrop;
    }

    @Column(name = "SIZE")
    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    @Column(name = "STATUS")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setFileUriCrop(String fileUriCrop) {
        this.fileUriCrop = fileUriCrop;
    }

    @Column(name = "CATEGORY")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Column(name = "CREATED_DATE")
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
