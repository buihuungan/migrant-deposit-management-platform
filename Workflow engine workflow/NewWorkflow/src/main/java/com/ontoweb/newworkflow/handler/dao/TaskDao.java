package com.ontoweb.newworkflow.handler.dao;

import com.ontoweb.newworkflow.core.model.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/10/7 下午3:29
 * @description：
 * @modified By：
 * @version: $
 */
public interface TaskDao extends JpaRepository<Task, Integer> {
    @Query(value = "SELECT *  FROM `uflo_task` task " +
            "WHERE task.`id_` IN (?1) " +
            "and (task.`assignee_` is null or task.`assignee_`  = '' ) " +
            "and IF ( ?2 != '', task.`process_id_` = ?2, '1=1' ) " +
            "and IF(?3 != '', task.`task_name_` = ?3, '1=1') " +
            "and IF(?4 != '', task.`create_date_` >= ?4, '1=1') " +
            "and IF(?5 != '', task.`create_date_` <= ?5, '1=1') " +
            "order by task.`create_date_` desc", nativeQuery = true)
    List<Task> getTakes(List<Long> ids, Long processId, String taskName, Date startTime, Date endTime);

    @Query(value = "SELECT * FROM `uflo_task` task " +
            "WHERE (task.`assignee_` = ?1 OR task.`owner_` = ?1) " +
            "and task.`end_date_` is null " +
            "and IF ( ?2 != '', task.`process_id_` = ?2, '1=1' ) " +
            "and IF(?3 != '', task.`task_name_` = ?3, '1=1') " +
            "and IF(?4 != '', task.`create_date_` >= ?4, '1=1') " +
            "and IF(?5 != '', task.`create_date_` <= ?5, '1=1') " +
            "order by task.`create_date_` desc ", nativeQuery = true)
    List<Task> getPendingTask(String loginUser, Long processId, String taskName, Date startTime, Date endTime);

    @Query(value = "select t from Task t where t.processInstanceId in (?1)")
    List<Task> findAllByProcessInstanceId(List<Long> ids);

    Task findByProcessInstanceIdAndTaskName(Long processInstanceId, String taskName);

    @Query(value = "select t from Task t where t.processInstanceId = ?1 and (t.endDate >= ?2 and t.endDate <= ?3) and t.taskName = ?4 and t.state = 'Withdraw'")
    List<Task> findPrevWithdrawTask(long processInstanceId, Date stratDate, Date endDate, String taskName);

    @Query(value = "select count(t.id) from Task t where t.processInstanceId = ?1 and t.state ='Completed' ")
    int getTaskNum(long processInstanceId);

    @Query(value = "select * from `uflo_task` where `PROCESS_INSTANCE_ID_` = ?1 order by `CREATE_DATE_` desc limit 1", nativeQuery = true)
    Task getTop1Task(long processId);

    @Query(value = "select id_ from `uflo_task` where `PROCESS_INSTANCE_ID_` = ?1 order by `CREATE_DATE_` desc limit 1", nativeQuery = true)
    String getNewTaskId(long processId);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from `uflo_task` where PROCESS_INSTANCE_ID_ =?1", nativeQuery = true)
    void deleteTaskByProcessInstanceId(long processInstanceId);

}
