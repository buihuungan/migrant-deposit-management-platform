package com.ontoweb.newworkflow.core.query;

import com.ontoweb.newworkflow.core.model.task.Task;
import com.ontoweb.newworkflow.core.model.task.TaskState;
import com.ontoweb.newworkflow.core.model.task.TaskType;

import java.util.Date;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/28 上午10:32
 * @description：任务查询
 * @modified By：
 * @version: $
 */
public interface TaskQuery extends Query<List<Task>> {
    TaskQuery assignee(String assignee);
    TaskQuery addAssignee(String assignee);
    TaskQuery owner(String owner);
    TaskQuery addTaskState(TaskState state);
    TaskQuery addPrevTaskState(TaskState state);
    TaskQuery processInstanceId(long processInstanceId);
    TaskQuery rootProcessInstanceId(long rootProcessInstanceId);
    TaskQuery createDateLessThen(Date date);
    TaskQuery createDateLessThenOrEquals(Date date);
    TaskQuery createDateGreaterThen(Date date);
    TaskQuery createDateGreaterThenOrEquals(Date date);
    TaskQuery dueDateLessThen(Date date);
    TaskQuery dueDateLessThenOrEquals(Date date);
    TaskQuery dueDateGreaterThen(Date date);
    TaskQuery dueDateGreaterThenOrEquals(Date date);
    TaskQuery urlLike(String url);
    TaskQuery subjectLike(String subject);
    TaskQuery countersign(boolean countersign);
    TaskQuery taskType(TaskType type);
    TaskQuery processId(long processId);
    TaskQuery addProcessId(long processId);
    TaskQuery nameLike(String name);
    TaskQuery nodeName(String nodeName);
    TaskQuery businessId(String businessId);
    TaskQuery page(int firstResult, int maxResults);
    TaskQuery addOrderAsc(String property);
    TaskQuery addOrderDesc(String property);
    TaskQuery addParticipator(String user);
    TaskQuery priority(String priority);
    TaskQuery progress(int progress);
}
