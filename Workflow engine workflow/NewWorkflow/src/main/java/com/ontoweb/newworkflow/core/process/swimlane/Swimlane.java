package com.ontoweb.newworkflow.core.process.swimlane;

import com.ontoweb.newworkflow.core.process.assign.Assignee;
import com.ontoweb.newworkflow.core.process.node.AssignmentType;

import java.io.Serializable;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/30 下午9:24
 * @description：泳道图
 * @modified By：
 * @version: $
 */
public class Swimlane implements Serializable {
    private static final long serialVersionUID = -2899019518648252638L;
    private String name;
    private String description;
    private AssignmentType assignmentType;
    private List<Assignee> assignees;
    private String expression;
    private String assignmentHandlerBean;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public List<Assignee> getAssignees() {
        return assignees;
    }
    public void setAssignees(List<Assignee> assignees) {
        this.assignees = assignees;
    }
    public String getAssignmentHandlerBean() {
        return assignmentHandlerBean;
    }
    public void setAssignmentHandlerBean(String assignmentHandlerBean) {
        this.assignmentHandlerBean = assignmentHandlerBean;
    }
    public AssignmentType getAssignmentType() {
        return assignmentType;
    }
    public void setAssignmentType(AssignmentType assignmentType) {
        this.assignmentType = assignmentType;
    }
    public String getExpression() {
        return expression;
    }
    public void setExpression(String expression) {
        this.expression = expression;
    }
}
