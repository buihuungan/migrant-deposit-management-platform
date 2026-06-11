package com.ontoweb.newworkflow.handler.assignee.assignInherit;


import com.ontoweb.newworkflow.handler.assignee.entity.AssigneeInfo;

import java.util.List;

public class InheritAssigneeInfo extends AssigneeInfo {

    private List<InheritAssignee> inheritAssignee;

    private String addProperty;

    public List<InheritAssignee> getInheritAssignee() {
        return this.inheritAssignee;
    }

    public void setInheritAssignee(List<InheritAssignee> inheritAssignee) {
        this.inheritAssignee = inheritAssignee;
    }

    public String getAddProperty() {
        return addProperty;
    }

    public void setAddProperty(String addProperty) {
        this.addProperty = addProperty;
    }
}
