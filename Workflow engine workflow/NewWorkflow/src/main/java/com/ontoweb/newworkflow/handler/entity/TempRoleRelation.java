package com.ontoweb.newworkflow.handler.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (TempRoleRelation)实体类
 *
 * @author wanheng
 * @since 2021-08-12 17:01:57
 */
@Data
public class TempRoleRelation implements Serializable {
    private static final long serialVersionUID = 977430109067805728L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    /**
     * 创建日期
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 更新人
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;
    /**
     * 更新日期
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    /**
     * 所属部门
     */
    private String sysOrgCode;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 角色id
     */
    private String roleId;

}
