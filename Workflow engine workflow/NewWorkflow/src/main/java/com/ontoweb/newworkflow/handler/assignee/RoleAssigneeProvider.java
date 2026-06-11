package com.ontoweb.newworkflow.handler.assignee;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.ProcessInstance;
import com.ontoweb.newworkflow.core.process.assign.Entity;
import com.ontoweb.newworkflow.core.process.assign.PageQuery;
import com.ontoweb.newworkflow.core.service.ProcessService;
import com.ontoweb.newworkflow.core.utils.EnvironmentUtils;
import com.ontoweb.newworkflow.handler.assignee.assignInherit.InheritAssigneeProvider;
import com.ontoweb.newworkflow.handler.assignee.assignInherit.InheritEntity;
import com.ontoweb.newworkflow.handler.assignee.entity.SysDepart;
import com.ontoweb.newworkflow.handler.assignee.entity.SysRole;
import com.ontoweb.newworkflow.handler.assignee.entity.SysUser;
import com.ontoweb.newworkflow.handler.service.BusinessService;
import com.ontoweb.newworkflow.handler.service.feign.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Author ethan yan
 * @Date 2020/11/10
 * @Desc 根据角色为管理员来作为审核人
 */
@Component
@Slf4j
public class RoleAssigneeProvider implements InheritAssigneeProvider {

    @Autowired
    private UserService userService;
    @Autowired
    private BusinessService businessService;
    
    @Autowired
    private ProcessService processService;


    @Override
    public boolean isTree() {
        return false;
    }

    @Override
    public String getName() {
        return "指定角色";
    }

    @Override
    public void queryEntities(PageQuery<Entity> pageQuery, String s) {

    }


    @Override
    public void queryInheritEntities(PageQuery<InheritEntity> pageQuery, String parentId) {
        log.info("getAllRoles");
        //通过调用用户体系微服务查询所有角色
        List<SysRole> allRoles = userService.queryall().getResult();
        int pageIndex = pageQuery.getPageIndex();
        int pageSize = pageQuery.getPageSize();
        //返回角色实体列表
        List<InheritEntity> entities = new ArrayList<>();

        InheritEntity parameter = (InheritEntity) pageQuery.getQueryParameter();
        String id = null;
        if (parameter != null) {
            id = parameter.getId();
        }

        // 分页 index为第i页
        int max = 0;
        if (allRoles.size() > pageIndex * pageSize) {
            max = pageIndex * pageSize;
        } else {
            max = allRoles.size();
        }
        for (int i = (pageIndex - 1) * pageSize; i < max; ++i) {
            SysRole sysRole = allRoles.get(i);
            if (id == null || String.valueOf(i).equals(id)) {
                InheritEntity entity = new InheritEntity(sysRole.getId(), sysRole.getRoleName());
                entities.add(entity);
            }
        }

        pageQuery.setRecordCount(allRoles.size());
        pageQuery.setResult(entities);
    }

    @Override
    public Collection<String> getUsers(String roleId, Context context, ProcessInstance processInstance) {
        //根据角色ID获取特定的指定参与者
        List<String> roleIds = new ArrayList<>();
        roleIds.add(roleId);
        String loginUser = EnvironmentUtils.getEnvironment().getLoginUser();
        JSONObject jsonEdu = new JSONObject();
        jsonEdu.put("userId", loginUser);
        jsonEdu.put("roleIds", roleIds);
//        eduService.grantRole(jsonEdu);
        log.info("roleId:{}" + roleId);


        //流程发起人属于的部门
        String lists = JSON.toJSONString(userService.getCurrentUserDeparts().getResult().get("list"));
        List<SysDepart> list = JSONObject.parseArray(lists, SysDepart.class);
        List<String> userDepart = new ArrayList<>();
        for(SysDepart sysDepart : list) {
            userDepart.add(sysDepart.getId());
        }

        String s = userService.userRoleList(roleId).toString();

        Map<String, Object> resultMap = (Map) JSONObject.parse(s);
        List<String> users = new ArrayList<>();
        users.add("e9ca23d68d884d4ebb19d07889727dae");
        Map result = (Map) resultMap.get("result");
        List<SysUser> sysUserList = (List<SysUser>) result.get("records");

        /*log.info("sysUserList:{}" + sysUserList);

        //由于服务端返回的是一个body类型的，要转还为实体类
        for (Object obj: sysUserList) {
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(obj));
            SysUser sysUser = jsonObject.toJavaObject(SysUser.class);
            String departIds = sysUser.getDepartIds();
            String[] departs = departIds.split(",");
            for (int i=0; i<departs.length; i++) {
                if (userDepart.contains(departs[i])) {
                    users.add(sysUser.getId());
                    break;
                }
            }
            break;
        }*/

        return users;
    }

    @Override
    public boolean disable() {
        return false;
    }
}
