package com.ontoweb.newworkflow.core.model.task;

import lombok.Data;

import java.util.Date;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 上午11:24
 * @description：任务摘要
 * @modified By：
 * @version: $
 */
@Data
public class TaskSummary {
    private long id;

    private String name;

    private String description;

    private String businessId;

    private TaskState state;

    private int priority;

    private String owner;

    private Date createDate;

    private long processInstanceId;

    private String processId;

    private long sessionId;
}
