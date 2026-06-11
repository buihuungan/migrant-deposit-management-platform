package com.ontoweb.newworkflow.handler.entity;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

/**
 * (SysUserDepart)实体类
 *
 * @author xzb
 * @since 2021-03-10 14:03:23
 */
public class SysUserDepart implements Serializable {
    private static final long serialVersionUID = -50186900371817219L;
    /**
     * id
     */
    @TableId
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 部门id
     */
    private String depId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDepId() {
        return depId;
    }

    public void setDepId(String depId) {
        this.depId = depId;
    }

}
