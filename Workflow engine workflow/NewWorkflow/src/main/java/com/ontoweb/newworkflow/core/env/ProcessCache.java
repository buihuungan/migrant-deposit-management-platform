package com.ontoweb.newworkflow.core.env;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:20
 * @description：
 * @modified By：
 * @version: $
 */
public interface ProcessCache {
    public static final String BEAN_ID="uflo.processCache";
    public static final String PROCESS_KEY="process_definition_key";
    public static final String CONTEXT_KEY="process_context_key";

    void store(String key,Object obj);
    Object retrive(String key);
}
