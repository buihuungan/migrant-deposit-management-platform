package com.ontoweb.newworkflow.handler.assignee;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.process.assign.Entity;
import com.ontoweb.newworkflow.core.process.assign.PageQuery;
import com.ontoweb.newworkflow.handler.assignee.assignInherit.InheritAssigneeProvider;
import com.ontoweb.newworkflow.handler.assignee.assignInherit.InheritEntity;
import com.ontoweb.newworkflow.handler.assignee.entity.SysDepart;
import com.ontoweb.newworkflow.handler.assignee.entity.SysUser;
import com.ontoweb.newworkflow.handler.service.feign.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Author ethan yan
 * @Date 2020/12/19
 * @Desc 根据用户体系直接选择参与者
 */
@Component
@Slf4j
public class UserAssigneeProvider implements InheritAssigneeProvider {

    @Autowired
    private UserService userService;

    @Override
    public boolean isTree() {
        return false;
    }

    @Override
    public String getName() {
        return "指定用户";
    }

    @Override
    public void queryEntities(PageQuery<Entity> pageQuery, String parentId) {

    }

    /**
     * 根据指定的处理人ID，返回具体的任务处理人用户名
     * @param userId 处理人ID，可能是一个用户的用户名，这样就是直接返回这个用户名，也可能是一个部门的ID，那么就是返回这个部门下的所有用户的用户名等
     * @param context context 流程上下文对象
     * @param processInstance 流程实例对象
     * @return 返回一个或多个任务处理人的ID
     */
    @Override
    public Collection<String> getUsers(String userId, Context context, ProcessInstance processInstance) {
        //SysUser sysUser = userService.queryById(userId).getResult();
        List<String> user = new ArrayList<>();
        user.add(userId);

        //userServiceImpl.sendMessageByUserIds("您有任务待处理，请查看任务列表", user);

        return user;
    }

    @Override
    public boolean disable() {
        return false;
    }

    @Override
    public void queryInheritEntities(PageQuery<InheritEntity> pageQuery, String parentId) {

        //显示所有部门信息
        List<SysDepart> listAll = userService.listAll("").getResult();
        Map<String, String> orgCodeToDepartNameMap = new HashMap<>();
        for (SysDepart sysDepart : listAll) {
            orgCodeToDepartNameMap.put(sysDepart.getOrgCode(), sysDepart.getDepartName());
        }

        //通过调用用户体系微服务查询所有角色
        String s = userService.queryPageList().toString();
        Map<String, Object> resultMap = (Map<String, Object>) JSONObject.parse(s);
        Map result = (Map) resultMap.get("result");
        List<SysUser> sysUserList = (List<SysUser>) result.get("records");
        List<SysUser> allUsers = new ArrayList<>();

        //由于服务端返回的是一个body类型的，要转还未
        for (Object obj: sysUserList) {
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(obj));
            SysUser sysUser = jsonObject.toJavaObject(SysUser.class);
            allUsers.add(sysUser);
        }

        int pageIndex = pageQuery.getPageIndex();
        int pageSize = pageQuery.getPageSize();
        List<InheritEntity> entities = new ArrayList<>();

        InheritEntity parameter = (InheritEntity) pageQuery.getQueryParameter();
        String id = null;
        if (parameter != null) {
            id = parameter.getId();
        }

// 分页 index为第i页
        int max = 0;
        if (allUsers.size() > pageIndex * pageSize) {
            max = pageIndex * pageSize;
        } else {
            max = allUsers.size();
        }
        for (int i = (pageIndex - 1) * pageSize; i < max; ++i) {
            SysUser sysUser = allUsers.get(i);
            if (id == null || String.valueOf(i).equals(id)) {
                sysUser.setOrgCode(orgCodeToDepartNameMap.get(sysUser.getOrgCode()));
                InheritEntity entity = new InheritEntity(sysUser.getId(), sysUser.getUsername(), JSON.toJSONString(sysUser));
                entities.add(entity);
            }
        }

        pageQuery.setRecordCount(allUsers.size());
        pageQuery.setResult(entities);
    }
}
