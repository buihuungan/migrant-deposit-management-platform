package com.ontoweb.newworkflow.handler.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/11/10 上午10:34
 * @description：
 * @modified By：
 * @version: $
 */
public interface DiagramService {
    JSONObject drawDiagram(long procInstId);
}
