package com.ontoweb.newworkflow.handler.assignee.assignInherit;

import com.ontoweb.newworkflow.core.process.assign.Entity;

/**
 * @Author Ethan yan
 * @Date 2020/12/19
 * @Desc 指定参与者显示内容过少，需要重写接口
 */
public class InheritEntity extends Entity {

    private String addProperty;

    public InheritEntity(String id, String name) {
        super(id, name);
    }

    public InheritEntity(String id, String name,String addProperty) {
        super(id, name);
        this.addProperty = addProperty;
    }

    public String getAddProperty() {
        return addProperty;
    }

    public void setAddProperty(String addProperty) {
        this.addProperty = addProperty;
    }
}
