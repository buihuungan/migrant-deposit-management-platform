package com.ontoweb.newworkflow.handler.dao;

import com.ontoweb.newworkflow.core.model.HistoryProcessInstance;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/10/4 下午8:45
 * @description：
 * @modified By：
 * @version: $
 */
public interface ProcessInstanceDao extends JpaRepository<ProcessInstance, Integer> {
    List<ProcessInstance> findAllByIdIn(Set<Long> ids);

    @Query("select ps from ProcessInstance ps where ps.id = ?1 ")
    ProcessInstance getById(Long id);

    /**
     *  针对创建时间来的，给定时间范围，根据create_date卡住
     * @param processId
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value = "select * from `uflo_process_instance` up " +
            "where IF ( ?1 != '', up.`process_id_` = ?1, '1=1' )  " +
            "and IF(?2 != '', up.`create_date_` >= ?2, '1=1') " +
            "and IF(?3 != '', up.`create_date_` <= ?3, '1=1') " +
            "order by up.`create_date_` desc", nativeQuery = true)
    List<ProcessInstance> findAll(Long processId, Date startTime, Date endTime);

    @Query(value = "select p from ProcessInstance p where p.processId = ?2 and p.promoter = ?1")
    ProcessInstance isStartClass(String loginUser, long processId);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from ProcessInstance p where p.processId =?2 and p.promoter =?1")
    void shutDownTask( String loginUser, Long processId);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from ProcessInstance p where p.id =?1")
    void deleteProcessInstanceById(long processInstanceId);

    @Query(value = "select p from ProcessInstance p where p.id = ?1")
    ProcessInstance findProcessInstanceById(long processInstanceId);
}
