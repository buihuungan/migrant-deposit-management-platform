package com.ontoweb.newworkflow.core.env.impl;

import com.ontoweb.newworkflow.core.env.ProcessCache;

import java.util.Hashtable;
import java.util.Map;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:21
 * @description：
 * @modified By：
 * @version: $
 */
public class MemoryProcessCache implements ProcessCache {

    private Map<String,Object> map = new Hashtable<String,Object>();

    @Override
    public void store(String key, Object obj) {
        map.put(key, obj);
    }

    @Override
    public Object retrive(String key) {
        return map.get(key);
    }
}
