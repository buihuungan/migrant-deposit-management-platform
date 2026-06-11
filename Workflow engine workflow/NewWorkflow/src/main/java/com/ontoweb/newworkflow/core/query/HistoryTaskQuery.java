package com.ontoweb.newworkflow.core.query;

import com.ontoweb.newworkflow.core.model.HistoryTask;
import com.ontoweb.newworkflow.core.model.task.TaskState;
import com.ontoweb.newworkflow.core.model.task.TaskType;

import java.util.Date;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:49
 * @description：
 * @modified By：
 * @version: $
 */
public interface HistoryTaskQuery extends Query<List<HistoryTask>> {
    HistoryTaskQuery assignee(String assignee);
    HistoryTaskQuery owner(String owner);
    HistoryTaskQuery addTaskState(TaskState state);
    HistoryTaskQuery addPrevTaskState(TaskState state);
    HistoryTaskQuery processInstanceId(long processInstanceId);
    HistoryTaskQuery rootProcessInstanceId(long rootProcessInstanceId);
    HistoryTaskQuery historyProcessInstanceId(long historyProcessInstanceId);
    HistoryTaskQuery createDateLessThen(Date date);
    HistoryTaskQuery createDateLessThenOrEquals(Date date);
    HistoryTaskQuery createDateGreaterThen(Date date);
    HistoryTaskQuery createDateGreaterThenOrEquals(Date date);
    HistoryTaskQuery endDateLessThen(Date date);
    HistoryTaskQuery endDateLessThenOrEquals(Date date);
    HistoryTaskQuery endDateGreaterThen(Date date);
    HistoryTaskQuery endDateGreaterThenOrEquals(Date date);
    HistoryTaskQuery urlLike(String url);
    HistoryTaskQuery countersign(boolean countersign);
    HistoryTaskQuery taskType(TaskType type);
    HistoryTaskQuery processId(long processId);
    HistoryTaskQuery taskId(long taskId);
    HistoryTaskQuery nameLike(String name);
    HistoryTaskQuery businessId(String businessId);
    HistoryTaskQuery nodeName(String nodeName);
    HistoryTaskQuery page(int firstResult, int maxResults);
    HistoryTaskQuery addOrderAsc(String property);
    HistoryTaskQuery addOrderDesc(String property);
}
