package com.ontoweb.newworkflow.handler.dao;

import com.ontoweb.newworkflow.core.model.HistoryTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/10/5 下午3:26
 * @description：
 * @modified By：
 * @version: $
 */
public interface HistoryTaskDao extends JpaRepository<HistoryTask, Integer> {
    @Query(value = "SELECT ht from HistoryTask ht where ht.historyProcessInstanceId in (?1)")
    List<HistoryTask> getProcessAssignee(List<Long> hisProcessInstanceId);

    @Query("select ht from HistoryTask ht where ht.processInstanceId = ?1 and ht.url is not null order by ht.endDate desc")
    List<HistoryTask> findByProcessInstanceId(Long processInstanceId);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from HistoryTask ht where ht.processInstanceId =?1")
    void deleteHistoryTaskByProcessInstanceId(long processInstanceId);
}
