package com.ontoweb.newworkflow.handler.dao;

import com.ontoweb.newworkflow.core.model.Blob;
import com.ontoweb.newworkflow.core.model.BlobTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;


/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/26 下午3:27
 * @description：流程模板的dao层应用
 * @modified By：
 * @version: $
 */
@Transactional
public interface BlobTempDao extends JpaRepository<BlobTemp, Integer> {
    @Query(value = "select id_ from uflo_blob_temp where name_ =?1", nativeQuery = true)
    Long findByName(String name);

    @Modifying
    @Query(value = "delete from uflo_blob_temp where id_ =?1", nativeQuery = true)
    void deleteUfloTemp(Long blobId);

    @Query(value = "select blob_value_ from uflo_blob_temp where name_ = ?1", nativeQuery = true)
    byte[] getByFileName(String fileName);

    @Query(value = "select bt from BlobTemp bt")
    List<BlobTemp> findUfloTempAll();

    @Query(value = "select bt from BlobTemp bt where bt.id = ?1")
    BlobTemp getByFileId(Long id);

    @Modifying
    @Query(value = "delete from BlobTemp bt where bt.id = ?1")
    void deleteFile(long id);

    @Modifying
    @Query(value = "update BlobTemp b set b.desc = ?2 where b.id = ?1")
    Integer updateDesc(long id, String desc);

    List<BlobTemp> findAllByOrderByCreateTimeDesc();

    @Query(value = "select bt from BlobTemp bt where bt.name = ?1")
    BlobTemp getWareIdByName(String name);

    @Query(value = "select bt from BlobTemp bt where bt.name = ?1")
    List<BlobTemp> getListByName(String name);

    @Query(value = "select bt from BlobTemp bt where bt.id = ?1")
    BlobTemp getListById(Long id);

    @Modifying
    @Query(value = "delete from BlobTemp bt where bt.name = ?1")
    void deleteFilebyName(String name);
}
