package com.ontoweb.newworkflow.handler.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author:ZhangD
 * @Description:
 * @Date: Created in 20:23 2022/1/5
 */
@Data
@Accessors(chain = true)
public class VideoVo {

    private String formDesignerId;

    private String onlineTableId;

    private String onlineDataId;
}
