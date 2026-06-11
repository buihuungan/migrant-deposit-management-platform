package com.ontoweb.newworkflow.handler.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/31 下午7:39
 * @description：流程设计服务类
 * @modified By：
 * @version: $
 */
public interface DesignerService {
    /**
     * 根据流程文件名称在blobTemp拿到json进行部署
     * @param jsonObject
     * @return
     */
    Boolean deployByFileName(JSONObject jsonObject);

    @Transactional
    Boolean deleteFile(Long id);

    JSONObject modifyFile(Long id);

    Boolean deleteFilebyName(String name);

}
