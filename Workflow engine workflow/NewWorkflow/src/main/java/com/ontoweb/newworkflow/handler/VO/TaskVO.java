package com.ontoweb.newworkflow.handler.VO;

import lombok.Data;

import java.util.Date;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/10/12 下午12:12
 * @description：任务前端显示
 * @modified By：
 * @version: $
 */
@Data
public class TaskVO {
    private Long id;
    private String description;
    private String nodeName;
    private String assignee;
    private Long processId;
    private Date createDate;
    private String state;
    private String type;
    private Long processInstanceId;
    private String processName;

    // 0 表示正常
    // 1 表示退回上一节点
    private Integer flag;
}
