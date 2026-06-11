package com.ontoweb.newworkflow.core.model.task.reminder;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 上午11:25
 * @description：任务提醒
 * @modified By：
 * @version: $
 */
@Entity
@Table(name="UFLO_TASK_REMINDER")
@Data
public class TaskReminder {
    @Id
    @Column(name="ID_")
    private long id;

    @Column(name="TASK_ID_")
    private long taskId;

    @Column(name="PROCESS_ID_")
    private long processId;

    @Column(name="CRON_",length=60)
    private String cron;

    @Column(name="TASK_NODE_NAME_",length=60)
    private String taskNodeName;

    @Column(name="REMINDER_HANDLER_BEAN_",length=120)
    private String reminderHandlerBean;

    @Column(name="REMINDER_TYPE_",length=20)
    @Enumerated(EnumType.STRING)
    private ReminderType type;

    @Column(name="START_DATE_")
    private Date startDate;
}
