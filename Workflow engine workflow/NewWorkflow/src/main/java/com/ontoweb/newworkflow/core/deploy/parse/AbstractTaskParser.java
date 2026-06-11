package com.ontoweb.newworkflow.core.deploy.parse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.process.security.Authority;
import com.ontoweb.newworkflow.core.process.security.ComponentAuthority;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:03
 * @description：任务抽象解析器
 * @modified By：
 * @version: $
 */
public abstract class AbstractTaskParser extends AbstractParser {
    protected List<ComponentAuthority> parseComponentAuthorities(JSONObject inputJson){
        List<ComponentAuthority> list=new ArrayList<ComponentAuthority>();


        if(!inputJson.getString("type").equals("component_authority")){
            return list;
        }
        String component=unescape(inputJson.getString("component"));
        Authority authority=Authority.valueOf(inputJson.getString("authority"));
        list.add(new ComponentAuthority(component,authority));

        return list;
    }
}
