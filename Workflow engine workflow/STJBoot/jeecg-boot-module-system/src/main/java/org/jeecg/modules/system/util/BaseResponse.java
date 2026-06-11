package org.jeecg.modules.system.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

/**
 * Created by zhangpan on 2017/5/23.
 */
@ApiModel("结果对象")
@ToString
public class BaseResponse {
    @ApiModelProperty("结果编码：默认0为成功 1为失败,-1异常")
    private int code;
    @ApiModelProperty("调用方可读的消息")
    private String msg;
    @ApiModelProperty("结果数据或异常调试信息")
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public BaseResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public BaseResponse() {

    }

    public BaseResponse(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
