package com.ontoweb.newworkflow.core.process.assign.impl;

import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.process.assign.Entity;
import com.ontoweb.newworkflow.core.process.assign.PageQuery;
import com.ontoweb.newworkflow.core.service.IdentityService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:39
 * @description：
 * @modified By：
 * @version: $
 */
public class UserAssigneeProvider extends AbstractAssigneeProvider {
    private IdentityService identityService;
    private boolean disabledUserAssigneeProvider;

    @Override
    public boolean isTree() {
        return false;
    }

    @Override
    public String getName() {
        return "指定用户";
    }
    public void queryEntities(PageQuery<Entity> pageQuery, String parentId) {
        identityService.userPageQuery(pageQuery);
    }

    public Collection<String> getUsers(String entityId, Context context, ProcessInstance processInstance) {
        List<String> users=new ArrayList<String>();
        users.add(entityId);
        return users;
    }

    @Override
    public boolean disable() {
        return disabledUserAssigneeProvider;
    }

    public void setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
    }
    public boolean isDisabledUserAssigneeProvider() {
        return disabledUserAssigneeProvider;
    }
    public void setDisabledUserAssigneeProvider(boolean disabledUserAssigneeProvider) {
        this.disabledUserAssigneeProvider = disabledUserAssigneeProvider;
    }
}
