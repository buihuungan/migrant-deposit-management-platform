package com.ontoweb.newworkflow.handler.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CictOfflineDataVo {


    private String userId;

    private String planId;

    private String lessonId;

    private String teachingMode;
}
