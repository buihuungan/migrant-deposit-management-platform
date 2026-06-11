package com.ontoweb.newworkflow.core.process.node;

import java.io.Serializable;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午9:35
 * @description：用户数据
 * @modified By：
 * @version: $
 */
public class UserData implements Serializable {
    private static final long serialVersionUID = -2747945192424126140L;
    private String key;
    private String value;

    public UserData(String key, String value) {
        super();
        this.key = key;
        this.value = value;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
