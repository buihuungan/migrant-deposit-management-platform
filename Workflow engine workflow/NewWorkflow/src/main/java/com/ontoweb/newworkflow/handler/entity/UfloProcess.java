package com.ontoweb.newworkflow.handler.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


import java.io.Serializable;
import java.util.Date;

@Data
@TableName("uflo_process")
public class UfloProcess implements Serializable {

    @TableId(value = "ID_")
    private long id;

    @TableField(value = "NAME_")
    private String name;

    @TableField(value = "KEY_")
    private String key;

    @TableField(value = "START_PROCESS_URL_")
    private String startProcessUrl;

    @TableField(value = "VERSION_")
    private int version;

    @TableField(value = "CREATE_DATE_")
    private Date createDate;

    @TableField(value = "EFFECT_DATE_")
    private Date effectDate;

    @TableField(value = "CATEGORY_ID_")
    private String categoryId;

    @TableField(value = "DESCRIPTION_")
    private String description;

    @TableField(value = "PROCESS_MAIN_TABLE_")
    private String processMainTable;

    @TableField(value = "PROCESS_MAIN_KEY_")
    private String processMainKey;

    @TableField(value = "MAIN_TABLE_ASSOCIATION_KEY_")
    private String mainTableAssociationKey;

    @TableField(value = "PROCESS_ASSOCIATION_TABLE_")
    private String processAssociationTable;

    @TableField(value = "PROCESS_ASSOCIATION_KEY_")
    private String processAssociationKey;



}
