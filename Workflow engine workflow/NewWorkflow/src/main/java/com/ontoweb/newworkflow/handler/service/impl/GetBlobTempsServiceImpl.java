package com.ontoweb.newworkflow.handler.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.model.BlobTemp;
import com.ontoweb.newworkflow.handler.dao.GetBlobTempsServiceCommand;
import com.ontoweb.newworkflow.handler.service.GetBlobTempsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/10/8 下午8:52
 * @description：
 * @modified By：
 * @version: $
 */
@Slf4j
@Service
public class GetBlobTempsServiceImpl implements GetBlobTempsService {
    @Autowired
    private GetBlobTempsServiceCommand getBlobTempsServiceCommand;

    @Override
    public List<BlobTemp> getTemps() {
        // 通过访问command接口获取列表
        List<BlobTemp> lists = getBlobTempsServiceCommand.getList();

        return lists;
    }
}
