package com.ontoweb.newworkflow.core.deploy.parse;

import com.alibaba.fastjson.JSONObject;
import org.dom4j.Element;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午5:05
 * @description：
 * @modified By：
 * @version: $
 */
public interface Parser {
    Object parse(JSONObject inputJson, long processId, boolean parseChildren);
    boolean support(JSONObject inputJson);
}
