package com.ontoweb.newworkflow.handler.entity;

import lombok.Data;

import java.util.List;

/**
 * @Author:ZhangD
 * @Description:
 * @Date: Created in 21:11 2022/1/4
 */
@Data
public class ContentVo {

    private String name;

    private String startProcessUrl;

    private String categoryId;

    private String effectDate;

    private String description;

    private String type;

    private String userId;

    private List<NodeListVo> nodeList;

}
