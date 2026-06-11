package com.ontoweb.newworkflow.core.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 上午11:11
 * @description：历史流程实例
 * @modified By：
 * @version: 1.0$
 */
@Entity
@Table(name="UFLO_HIS_PROCESS_INSTANCE")
@Data
public class HistoryProcessInstance {
    @Id
    @Column(name="ID_")
    private long id;

    @Column(name="PROCESS_ID_")
    private long processId;

    @Column(name="BUSINESS_ID_",length=60)
    private String businessId;

    @Column(name="SUBJECT_",length=200)
    private String subject;

    @Column(name="CREATE_DATE_")
    private Date createDate;

    @Column(name="PROCESS_INSTANCE_ID_")
    private long processInstanceId;

    @Column(name="END_DATE_")
    private Date endDate;

    @Column(name="TAG_",length=30)
    private String tag;

    @Column(name="PROMOTER_",length=60)
    private String promoter;
}
