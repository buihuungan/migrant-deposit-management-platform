package com.ontoweb.newworkflow.core.model.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ontoweb.newworkflow.core.model.Activity;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 上午11:21
 * @description：任务
 * @modified By：
 * @version: $
 */
@Entity
@Table(name="UFLO_TASK")
@Data
public class Task extends Activity {
    @Column(name="TASK_NAME_",length=60)
    private String taskName;

    @Column(name="ASSIGNEE_",length=60)
    private String assignee;

    @Column(name="OWNER_",length=60)
    private String owner;

    @Column(name="PROGRESS_")
    private Integer progress;

    @Enumerated(EnumType.STRING)
    @Column(name="STATE_",length=20)
    private TaskState state;

    @Column(name="PRIORITY_",length=20)
    private String priority;

    @Enumerated(EnumType.STRING)
    @Column(name="PREV_STATE_",length=20)
    private TaskState prevState;

    @Column(name="PROCESS_INSTANCE_ID_")
    private long processInstanceId;

    @Column(name="ROOT_PROCESS_INSTANCE_ID_")
    private long rootProcessInstanceId;

    @Column(name="CREATE_DATE_")
    private Date createDate;

    @Column(name="END_DATE_")
    private Date endDate;

    @Column(name="PREV_TASK_",length=60)
    private String prevTask;

    @Column(name="OPINION_",length=200)
    private String opinion;

    @Column(name="URL_",length=120)
    private String url;

    @Column(name="SUBJECT_",length=200)
    private String subject;

    @Column(name="TYPE_",length=20)
    @Enumerated(EnumType.STRING)
    private TaskType type;

    @Column(name="COUNTERSIGN_COUNT_")
    private int countersignCount;

    @Column(name="DUEDATE_")
    private Date duedate;

    @Column(name="DUE_ACTION_DATE_")
    private Date dueActionDate;

    @Column(name="DATE_UNIT_",length=20)
    @Enumerated(EnumType.STRING)
    private DateUnit dateUnit;

    @Column(name="BUSINESS_ID_",length=60)
    private String businessId;

    @JsonIgnore
    @OneToMany(cascade=CascadeType.DETACH,fetch=FetchType.LAZY)
    @JoinColumn(name="TASK_ID_")
    private Collection<TaskParticipator> taskParticipators;
}
