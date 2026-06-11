package com.ontoweb.newworkflow.core.model.task;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 上午11:22
 * @description：任务指定者
 * @modified By：
 * @version: $
 */
@Entity
@Table(name="UFLO_TASK_APPOINTOR")
@Data
public class TaskAppointor {
    @Id
    @Column(name="ID_")
    private long id;

    @Column(name="PROCESS_INSTANCE_ID_")
    private long processInstanceId;

    @Column(name="TASK_NODE_NAME_",length=60)
    private String taskNodeName;

    @Column(name="OWNER_",length=60)
    private String owner;

    @Column(name="APPOINTOR_",length=60)
    private String appointor;

    @Column(name="APPOINTOR_NODE_",length=60)
    private String appointorNode;
}
