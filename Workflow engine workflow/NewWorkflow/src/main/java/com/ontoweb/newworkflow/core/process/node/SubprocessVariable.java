package com.ontoweb.newworkflow.core.process.node;

import java.io.Serializable;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午4:08
 * @description：
 * @modified By：
 * @version: $
 */
public class SubprocessVariable implements Serializable {
    private static final long serialVersionUID = 1L;
    private String inParameterKey;
    private String outParameterKey;
    public SubprocessVariable(String inParameterKey,String outParameterKey){
        this.inParameterKey=inParameterKey;
        this.outParameterKey=outParameterKey;
    }
    public String getInParameterKey() {
        return inParameterKey;
    }
    public void setInParameterKey(String inParameterKey) {
        this.inParameterKey = inParameterKey;
    }
    public String getOutParameterKey() {
        return outParameterKey;
    }
    public void setOutParameterKey(String outParameterKey) {
        this.outParameterKey = outParameterKey;
    }
}
