package com.wust.businessback.service;

import com.alibaba.fastjson.JSONObject;
import com.wust.businessback.constant.Result;

public interface ShortLetterService {

    //安全接口 发送单条信息
    public Result sendSingleSMS(JSONObject jsonObject);

    //安全接口 批量发送短息
    public Result sendBatchSMS(JSONObject jsonObject);


}
