package com.ontoweb.newworkflow.handler.dao;

import com.ontoweb.newworkflow.core.model.HistoryProcessInstance;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/10/4 下午8:51
 * @description：
 * @modified By：
 * @version: $
 */
public interface HistoryProcessInstanceDao extends JpaRepository<HistoryProcessInstance, Integer> {
    @Query(value = "select hp from HistoryProcessInstance hp where hp.endDate is not null and hp.tag = 'cancel' ")
    List<HistoryProcessInstance> getCancelProcess();

    /**
     * 已拒绝的流程排序是按照创建时间的降序排列，后期如果有调整可以在这个地方进行调整
     * @param ids
     * @return
     */
//    @Query(value = "select * from `uflo_his_process_instance` hp where hp.`id_` in (?1) and IF ( ?2 != '', hp.`process_id_` = ?2, '1=1' )  and IF(?3 != '', hp.`create_date_` <= ?3, '1=1') and IF(?4 != '', hp.`end_date_` >= ?4, '1=1') order by hp.`create_date_` desc", nativeQuery = true)
//    List<HistoryProcessInstance> findAllByIdIn(Set<Long> ids, Long processId, Date startTime, Date endTime);
    @Query(value = "SELECT * FROM `uflo_his_process_instance` hp WHERE hp.`id_` IN (?1) " +
            "AND (?2 IS NULL OR hp.`process_id_` = ?2) " +
            "AND (?3 IS NULL OR hp.`create_date_` <= ?3) " +
            "AND (?4 IS NULL OR hp.`end_date_` >= ?4) " +
            "ORDER BY hp.`create_date_` DESC", nativeQuery = true)
    List<HistoryProcessInstance> findAllByIdIn(Set<Long> ids, Long processId, Date startTime, Date endTime);


    @Query(value = "select * from `uflo_his_process_instance` hp where hp.`end_date_` is not null " +
            "and hp.`tag_` is null and IF ( ?1 != '', hp.`process_id_` = ?1, '1=1' )  " +
            "and IF(?2 != '', hp.`create_date_` <= ?2, '1=1') " +
            "and IF(?3 != '', hp.`end_date_` >= ?3, '1=1') " +
            "order by hp.`create_date_` desc", nativeQuery = true)
    List<HistoryProcessInstance> getCompleteProcessInstances(Long processId, Date startTime, Date endTime);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from HistoryProcessInstance hp where hp.processInstanceId =?1")
    void deleteHistoryProcessInstanceByProcessInstanceId(long processInstanceId);
}
