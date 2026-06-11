package com.ontoweb.newworkflow.handler.entity;

import lombok.Data;

/**
 * @Author:ZhangD
 * @Description:
 * @Date: Created in 21:17 2022/1/4
 */
@Data
public class NodeListVo {

    private String id;

    private String type;

    private String name;

    private String left;

    private String top;

    private String taskType;

    private String assignmentType;

    private String assignee;

    private String state;

    private String feature;

    private String formDesignerId;

    private String onlineTableId;

    private String onlineDataId;

    private String company;

}
