package com.ontoweb.newworkflow.handler.assignee.entity;

import com.ontoweb.newworkflow.core.process.assign.Assignee;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/10/12 上午9:23
 * @description：
 * @modified By：
 * @version: $
 */
public class AssigneeInfo {
    private List<Assignee> assignees;
    private String name;
    private int count;
    private boolean tree;
    private String providerId;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Assignee> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<Assignee> assignees) {
        this.assignees = assignees;
    }

    public boolean isTree() {
        return tree;
    }

    public void setTree(boolean tree) {
        this.tree = tree;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
}
