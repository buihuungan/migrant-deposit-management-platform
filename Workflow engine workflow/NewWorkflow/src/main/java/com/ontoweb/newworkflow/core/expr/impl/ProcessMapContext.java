package com.ontoweb.newworkflow.core.expr.impl;


import org.apache.commons.jexl2.MapContext;

import java.io.Serializable;
import java.util.Map;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午3:52
 * @description：
 * @modified By：
 * @version: $
 */
public class ProcessMapContext extends MapContext implements Serializable {
    private static final long serialVersionUID = -6102470247244550707L;

    public Map<String,Object> getMap(){
        return map;
    }
}
