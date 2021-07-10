package com.project.upload.repository;

import com.project.upload.constant.Constants;
import com.project.upload.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    /**
     * Get all files
     *
     * @return {@link List<FileEntity>}
     */
    @Query("SELECT fe FROM FileEntity fe WHERE fe.status = " + Constants.FILE.STATUS.FIREBASE.EXIST)
    List<FileEntity> findAllFiles();

    /**
     * Update file status if file doesn't exist on firebase anymore
     *
     * @param id - file id
     */
    @Modifying
    @Query("UPDATE FileEntity fe SET fe.status = " + Constants.FILE.STATUS.FIREBASE.NOT_EXIST + " WHERE fe.id = :id")
    void updateFileStatusNotExist(@Param(value = "id") Long id);
}
