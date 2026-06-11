package com.ontoweb.newworkflow.handler.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (TeachingContent)实体类
 *
 * @author wanheng
 * @since 2021-08-12 16:59:13
 */
@Data
@Builder
public class TeachingContent implements Serializable {
    private static final long serialVersionUID = -97359904797058232L;

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
     * 学生用户id
     */
    private String userId;
    /**
     * 教学计划id
     */
    private String planId;
    /**
     * 教学内容
     */
    private String planContent;

}
