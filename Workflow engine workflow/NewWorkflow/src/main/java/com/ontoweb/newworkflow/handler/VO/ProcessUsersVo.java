package com.ontoweb.newworkflow.handler.VO;

import lombok.Data;

import java.util.List;

/**
 * @Author:ZhangD
 * @Description:
 * @Date: Created in 20:11 2022/3/30
 */
@Data
public class ProcessUsersVo {

    private String wareName;

    private String planId;

    private String lessonId;

    private String teachingMode;

    private List<String> loginUserList;
}
