package com.ontoweb.newworkflow.core.model;

import com.ontoweb.newworkflow.core.model.task.TaskState;
import com.ontoweb.newworkflow.core.model.task.TaskType;
import lombok.Data;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 上午11:11
 * @description：历史任务
 * @modified By：
 * @version: 1.0$
 */
@XmlRootElement(name="HistoryTask")
@Entity
@Table(name="UFLO_HIS_TASK")
@Data
public class HistoryTask extends Activity{
    @Enumerated(EnumType.STRING)
    @Column(name="STATE_",length=20)
    private TaskState state;

    @Column(name="TASK_NAME_",length=60)
    private String taskName;

    @Column(name="CREATE_DATE_")
    private Date createDate;

    @Column(name="END_DATE_")
    private Date endDate;

    @Column(name="OWNER_",length=60)
    private String owner;

    @Column(name="ASSIGNEE_",length=60)
    private String assignee;

    @Column(name="TYPE_",length=20)
    @Enumerated(EnumType.STRING)
    private TaskType type;

    @Column(name="DUEDATE_")
    private Date duedate;

    @Column(name="HIS_PROCESS_INSTANCE_ID_")
    private long historyProcessInstanceId;

    @Column(name="PROCESS_INSTANCE_ID_")
    private long processInstanceId;

    @Column(name="ROOT_PROCESS_INSTANCE_ID_")
    private long rootProcessInstanceId;

    @Column(name="TASK_ID_")
    private long taskId;

    @Column(name="OPINION_",length=200)
    private String opinion;

    @Column(name="URL_",length=120)
    private String url;

    @Column(name="BUSINESS_ID_",length=60)
    private String businessId;

    @Column(name="SUBJECT_",length=200)
    private String subject;
}
