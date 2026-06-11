package com.ontoweb.newworkflow.handler.VO;

import lombok.Data;

/**
 * @Author:ZhangD
 * @Description:
 * @Date: Created in 10:05 2022/1/19
 */
@Data
public class ProcessVo {

    private Long processId;

    private String planId;

    private String userId;

    private String lessonId;

    private String teachingMode;
}
