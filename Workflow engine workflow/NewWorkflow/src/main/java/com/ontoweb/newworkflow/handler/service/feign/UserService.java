package com.ontoweb.newworkflow.handler.service.feign;

import com.alibaba.fastjson.JSONObject;
import com.ontoweb.newworkflow.handler.assignee.entity.SysDepart;
import com.ontoweb.newworkflow.handler.assignee.entity.SysRole;
import com.ontoweb.newworkflow.handler.assignee.entity.SysUser;
import com.ontoweb.newworkflow.handler.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author ethan yan
 * @Date 2020/11/10
 * @Desc 调用用户体系的微服务
 */
@Service
@FeignClient(value = "STJ-BOOT",contextId = "user")
public interface UserService {

    /**
     * 获取所有角色
     * @return
     */
    @GetMapping(value = "/stj/sys/role/queryall")
    Result<List<SysRole>> queryall();

    /**
     * 根据角色获取所有用户
     * @param roleId
     * @return
     */
    @GetMapping("/stj/sys/user/userRoleList")
    JSONObject userRoleList(@RequestParam(name = "roleId") String roleId);

    @GetMapping("/stj/sys/user/getCurrentUserDeparts")
    Result<Map<String,Object>> getCurrentUserDeparts();

    /**
     * 获取所有用户
     * @return
     */
    @GetMapping("/stj/sys/user/list")
    JSONObject queryPageList();

    /**
     * 根据用户ID获取到具体的用户
     * @param id
     * @return
     */
    @GetMapping("/stj/sys/user/queryById")
    Result<SysUser> queryById(@RequestParam(name = "id", required = true) String id);

    /**
     * 当前显示的单位编码，要显示中文名称
     * @param id
     * @return
     */
    @GetMapping("/stj/sys/sysDepart/listAll")
    Result<List<SysDepart>> listAll(@RequestParam(name = "id", required = false) String id);

    /**
     * 保存表单数据
     *
     * @param name
     * @param jsonObject
     * @return
     */
    @PostMapping({"/stj/online/cgform/api/crazyForm/{name}"})
    Result<?> addCrazyForm(@PathVariable("name") String name, @RequestBody JSONObject jsonObject);

    @GetMapping(value = "/stj/sys/user/queryUserRole")
    Result<List<String>> queryUserRole(@RequestParam(name = "userid", required = true) String userid);

    @GetMapping(value = "/stj/sys/user/userDepartList")
    Result<List<JSONObject>> getUserDepartsList(@RequestParam(name = "userId", required = true) String userId);
//    @PostMapping("/stj/sys/api/sendSysAnnouncement")
//    Result<String> sendUser(@RequestBody MessageDTO message);

    @GetMapping("/stj/sys/role/getRoleNameByUserId/{userId}")
    String getRoleNameByUserId(@PathVariable("userId") String userId);

}
