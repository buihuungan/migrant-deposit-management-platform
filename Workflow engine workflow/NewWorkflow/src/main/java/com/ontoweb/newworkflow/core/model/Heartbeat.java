package com.ontoweb.newworkflow.core.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 上午11:08
 * @description： 应用于集群部署
 * @modified By：
 * @version: 1.0$
 */
@Entity
@Table(name="UFLO_JOB_HEARTBEAT")
@Data
public class Heartbeat implements Serializable {
    private static final long serialVersionUID = 3256795533221464499L;
    @Id
    @Column(name="ID_",length=60)
    private String id;
    @Column(name="INSTANCE_NAME_",length=60)
    private String instanceName;
    @Column(name="DATE_")
    private Date date;
}
