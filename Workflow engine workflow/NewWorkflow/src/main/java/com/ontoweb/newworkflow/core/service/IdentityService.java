package com.ontoweb.newworkflow.core.service;

import com.ontoweb.newworkflow.core.process.assign.Entity;
import com.ontoweb.newworkflow.core.process.assign.PageQuery;

import java.util.Collection;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/28 上午10:13
 * @description：hibernate标识部分
 * @modified By：
 * @version: $
 */
public interface IdentityService {
    public static final String USER_TYPE="user";
    public static final String DEPT_TYPE="udept";
    public static final String POSITION_TYPE="position";
    public static final String GROUP_TYPE="group";
    public static final String DEPT_POSITION_TYPE="dept-position";

//    public static final String BEAN_ID="uflo.identityService";
    void userPageQuery(PageQuery<Entity> query);
    void deptPageQuery(PageQuery<Entity> query, String parentId);
    void positionPageQuery(PageQuery<Entity> query,String parentId);
    void groupPageQuery(PageQuery<Entity> query,String parentId);

    Collection<String> getUsersByGroup(String group);
    Collection<String> getUsersByPosition(String position);
    Collection<String> getUsersByDept(String dept);
    Collection<String> getUsersByDeptAndPosition(String dept,String position);
}
