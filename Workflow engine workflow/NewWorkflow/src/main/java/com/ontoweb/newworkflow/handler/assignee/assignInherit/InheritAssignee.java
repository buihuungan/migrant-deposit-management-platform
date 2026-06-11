package com.ontoweb.newworkflow.handler.assignee.assignInherit;

import com.ontoweb.newworkflow.core.process.assign.Assignee;

public class InheritAssignee extends Assignee {
    private String addProperty;

    public String getAddProperty() {
        return addProperty;
    }
    public void setAddProperty(String providerId) {
        this.addProperty = providerId;
    }
}
