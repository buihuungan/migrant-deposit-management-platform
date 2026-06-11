package com.ontoweb.newworkflow.core.process.assign;

import java.io.Serializable;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午9:25
 * @description：指定参与者
 * @modified By：
 * @version: $
 */
public class Assignee implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String providerId;
    private boolean chosen;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getProviderId() {
        return providerId;
    }
    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
    public boolean isChosen() {
        return chosen;
    }
    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }
}
