package com.ontoweb.newworkflow.handler.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/10/11 下午5:19
 * @description：流程列表前端展示
 * @modified By：
 * @version: $
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessListVO {
    private Long processId;
    private String name;
    private String key;
    private int version;
    private String categoryId;
    private String description;
    private Date createDate;
    private Date effectDate;
}
