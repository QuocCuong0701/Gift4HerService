package com.project.upload.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "FILE")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_ID")
    private Long id;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "FILE_URI_ORIGINAL")
    private String fileUriOriginal;

    @Column(name = "FILE_URI_CROP")
    private String fileUriCrop;

    @Column(name = "SIZE")
    private Double size;

    @Column(name = "STATUS")
    private int status;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "ORDER_NO")
    private Integer orderNo;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    public FileEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUriOriginal() {
        return fileUriOriginal;
    }

    public void setFileUriOriginal(String fileUriOriginal) {
        this.fileUriOriginal = fileUriOriginal;
    }

    public String getFileUriCrop() {
        return fileUriCrop;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setFileUriCrop(String fileUriCrop) {
        this.fileUriCrop = fileUriCrop;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
