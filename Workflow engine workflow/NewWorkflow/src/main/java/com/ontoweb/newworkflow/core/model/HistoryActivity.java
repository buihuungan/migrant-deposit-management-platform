package com.ontoweb.newworkflow.core.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 上午11:09
 * @description：历史活动
 * @modified By：
 * @version: 1.0$
 */
@Entity
@Table(name="UFLO_HIS_ACTIVITY")
@Data
public class HistoryActivity extends Activity{
    @Column(name="CREATE_DATE_")
    private Date createDate;

    @Column(name="END_DATE_")
    private Date endDate;

    @Column(name="PROCESS_INSTANCE_ID_")
    private long processInstanceId;

    @Column(name="ROOT_PROCESS_INSTANCE_ID_")
    private long rootProcessInstanceId;

    @Column(name="HIS_PROCESS_INSTANCE_ID_")
    private long historyProcessInstanceId;

    @Column(name="LEAVE_FLOW_NAME_",length=60)
    private String leaveFlowName;
}
