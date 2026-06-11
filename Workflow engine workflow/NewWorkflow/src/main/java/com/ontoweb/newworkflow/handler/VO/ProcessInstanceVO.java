package com.ontoweb.newworkflow.handler.VO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/10/13 上午9:42
 * @description：流程实例的前端展示字段
 * @modified By：
 * @version: $
 */
@Data
@NoArgsConstructor
public class ProcessInstanceVO {
    private Long id;
    private Date createDate;
    private String processName;
    private String promoter;
    private String currentTask;
    private String state;
    private long processInstanceId;
}
