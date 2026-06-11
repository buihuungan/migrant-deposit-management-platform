package com.ontoweb.newworkflow.margin.service;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.handler.utils.Result;

public interface ShortLetterService {

    //安全接口 发送单条信息
    public Result sendSingleSMS(JSONObject jsonObject);

    //安全接口 批量发送短息
    public Result sendBatchSMS(JSONObject jsonObject);


}
