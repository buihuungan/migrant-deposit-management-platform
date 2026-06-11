package com.ontoweb.newworkflow.handler.dao;

import com.ontoweb.newworkflow.core.model.task.TaskParticipator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/10/7 下午3:32
 * @description：
 * @modified By：
 * @version: $
 */
public interface TaskParticipatorDao extends JpaRepository<TaskParticipator, Integer> {
    List<TaskParticipator> findTaskParticipatorsByUser(String userId);

    /**
     * 通过任务id删除记录
     */
    @Modifying
    @Query(value = "delete from TaskParticipator tp where tp.taskId = ?1")
    Integer deleteByTaskId(Long taskId);
}
