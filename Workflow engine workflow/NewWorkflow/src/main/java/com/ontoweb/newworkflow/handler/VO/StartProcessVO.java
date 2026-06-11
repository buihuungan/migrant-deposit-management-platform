package com.ontoweb.newworkflow.handler.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/10/8 下午10:19
 * @description：开启流程时返回给前端的内容
 * @modified By：
 * @version: $
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartProcessVO {
    private long processInstanceId;
    private String formDesignerId;
    private String onlineTableId;
    private String onlineDataId;
    private String currentTask;
    private long processId;
    private String businessId;
}
