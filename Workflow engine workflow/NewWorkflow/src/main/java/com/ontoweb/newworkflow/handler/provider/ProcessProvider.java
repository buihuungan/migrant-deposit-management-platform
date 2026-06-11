package com.ontoweb.newworkflow.handler.provider;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/4 下午5:13
 * @description：流程相关提供的接口
 * @modified By：
 * @version: $
 */
public interface ProcessProvider {
    InputStream loadProcess(String fileName);
    List<ProcessFile> loadAllProcesses();
    Boolean saveProcess(JSONObject jsonObject);
    void deleteProcess(String fileName);
    String getName();
    String getPrefix();
    boolean support(String fileName);
    boolean isDisabled();
}
