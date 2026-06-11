package com.ontoweb.newworkflow.core.model.task;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 上午11:23
 * @description：任务参与者
 * @modified By：
 * @version: $
 */
@Entity
@Table(name="UFLO_TASK_PARTICIPATOR")
@Data
public class TaskParticipator {
    @Id
    @Column(name="ID_")
    private long id;

    @Column(name="TASK_ID_",nullable=false)
    private long taskId;

    @Column(name="USER_",length=60)
    private String user;
}
