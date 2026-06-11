package com.ontoweb.newworkflow.handler.entity;

import com.ontoweb.newworkflow.core.service.impl.job.ScanReminderJob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/11/10 上午11:10
 * @description：画图实体类
 * @modified By：
 * @version: $
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Diagram {
    private long id;
    private String nodeName;
    private long processId;
    private Date createDate;
    private Date endDate;
    private long taskId;
    private String businessId;
    private String assignee;
    private String type;
    private String prevTask;
    private String url;
    private String state;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
}
