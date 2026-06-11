package com.ontoweb.newworkflow.handler.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ontoweb.newworkflow.handler.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 陈德宇
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * 删除用户角色信息
     * @param userId
     * @return
     */
    int deleteByUserId(@Param("userId") String userId);

}
