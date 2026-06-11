package com.ontoweb.newworkflow.handler.VO;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;
@Data
public class  ProcessAllVO {
    //任务id
    private Long taskId;
    //流程id
    private Long processId;
    //流程实例id
    private Long processInstanceId;
    //流程历史实例id
    private Long processHisInstanceId;
    //节点名称
    private String nodeName;
    //流程名称
    private String processName;
    //创建时间
    private Date createDate;
    //结束时间
    private Date endDate;
    //节点状态
    private String state;
    //节点类型
    private String type;
    // 0 表示正常
    // 1 表示退回上一节点
    private Integer flag;


    //流程主表
    private String processMainTable;
    //流程主表-主键
    private String processMainKey;
    //流程类型
    private String dataType;
    //拼接数据
    private JSONObject allData;
}
