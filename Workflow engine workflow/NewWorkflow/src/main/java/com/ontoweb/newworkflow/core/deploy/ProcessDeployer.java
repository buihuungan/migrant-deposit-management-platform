package com.ontoweb.newworkflow.core.deploy;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.model.ProcessDefinition;

import java.io.InputStream;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午8:39
 * @description：
 * @modified By：
 * @version: $
 */
public interface ProcessDeployer {
    public static final String BEAN_ID="uflo.processDeployer";
    public static final String PROCESS_EXTENSION_NAME=".uflo.xml";
    public static final String PROCESSIMG_EXTENSION_NAME=".png";
    ProcessDefinition deploy(JSONObject inputJson);
    ProcessDefinition deploy(JSONObject inputJson, int version, long processId);
}
