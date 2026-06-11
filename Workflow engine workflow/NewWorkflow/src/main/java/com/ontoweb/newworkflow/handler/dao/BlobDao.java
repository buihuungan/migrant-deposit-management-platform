package com.ontoweb.newworkflow.handler.dao;

import com.ontoweb.newworkflow.core.model.Blob;
import com.ontoweb.newworkflow.core.model.BlobTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;
import java.util.List;


/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/26 下午3:27
 * @description：流程模板的dao层应用
 * @modified By：
 * @version: $
 */
public interface BlobDao extends JpaRepository<Blob, Integer> {
    /**
     * 根据流程ID获取blob记录
     * @param processId
     * @return
     */
    @Query(value = "select id_ from uflo_blob where process_id_ =?1", nativeQuery = true)
    Long findByProcessId(Long processId);

    @Modifying
    @Query(value = "delete from uflo_blob where id_ =?1", nativeQuery = true)
    void deleteUflo(Long blobId);

    @Query(value = "select b from Blob b where b.processId =?1")
    Blob findblobProcessId(Long processId);

    @Query(value = "select b from Blob b")
    List<Blob> findblobAll();

    @Query(value = "select b from Blob b where b.id =?1")
    Blob getNumOfProcess(Long identification);

    @Query(value = "select b from Blob b where b.name =?1")
    Blob getIdByName(String name);

    @Query(value = "select b from Blob b where b.name =?1")
    Blob selectBlobByWareId(String wareName);

    @Query(value = "select b from Blob b where b.name=?1")
    Blob selectProcessIdByWareName(String wareId);

    //根据name查询得到List
    @Query(value = "select b from Blob b where b.name=?1")
    List<Blob> getListByName(String name);

    @Modifying
    @Query(value = "delete from Blob b where b.name =?1")
    void deleteUflobyName(String name);

}
