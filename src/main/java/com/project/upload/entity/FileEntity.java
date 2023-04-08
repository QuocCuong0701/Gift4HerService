package com.project.upload.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "FILE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
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
    private Long size;

    @Column(name = "STATUS")
    private int status;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "ORDER_NO")
    private Integer orderNo;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileEntity that = (FileEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
