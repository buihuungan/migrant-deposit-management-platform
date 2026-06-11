package com.ontoweb.newworkflow.handler.service;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.model.BlobTemp;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/10/8 下午8:52
 * @description：
 * @modified By：
 * @version: $
 */
public interface GetBlobTempsService {
    List<BlobTemp> getTemps();
}
