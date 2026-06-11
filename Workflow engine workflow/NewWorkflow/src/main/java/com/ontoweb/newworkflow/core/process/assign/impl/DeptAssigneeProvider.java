package com.ontoweb.newworkflow.core.process.assign.impl;

import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.process.assign.Entity;
import com.ontoweb.newworkflow.core.process.assign.PageQuery;
import com.ontoweb.newworkflow.core.service.IdentityService;

import java.util.Collection;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:38
 * @description：
 * @modified By：
 * @version: $
 */
public class DeptAssigneeProvider extends AbstractAssigneeProvider {
    private IdentityService identityService;
    private boolean disabledDeptAssigneeProvider;

    @Override
    public boolean isTree() {
        return true;
    }

    @Override
    public String getName() {
        return "指定部门";
    }

    @Override
    public void queryEntities(PageQuery<Entity> pageQuery, String parentId) {
        identityService.deptPageQuery(pageQuery,parentId);
    }

    @Override
    public Collection<String> getUsers(String entityId,Context context,ProcessInstance processInstance) {
        return identityService.getUsersByDept(entityId);
    }

    @Override
    public boolean disable() {
        return disabledDeptAssigneeProvider;
    }

    public void setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
    }

    public boolean isDisabledDeptAssigneeProvider() {
        return disabledDeptAssigneeProvider;
    }

    public void setDisabledDeptAssigneeProvider(boolean disabledDeptAssigneeProvider) {
        this.disabledDeptAssigneeProvider = disabledDeptAssigneeProvider;
    }
}
