package com.ontoweb.newworkflow.handler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
@Data
public class ProcessMapping implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //流程主表id
    private String mainId;
    //流程id
    private Long processId;
    //流程实例id
    private Long processInstanceId;
    //流程的历史实例id
    private Long processHisInstanceId;
    //主表名称
    private String tableName;
}
