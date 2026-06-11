package com.ontoweb.newworkflow.handler.VO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author:ZhangD
 * @Description:
 * @Date: Created in 11:45 2022/1/18
 */
@Data
public class EduPlanDetailVo {


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
     * 计划ID
     */
    private String planId;
    /**
     * 排序
     */
    private Integer orderNum;
    /**
     * 教师ID
     */
    private String teacherId;
    /**
     * 教师工号
     */
    private String teacherCode;
    /**
     * 教师名
     */
    private String teacherName;
    /**
     * 课程ID
     */
    private String lessonId;
    /**
     * 课程名称
     */
    private String lessonName;
    /**
     * 仿真课件ID
     */
    private String wareId;
    /**
     * 仿真课件名称
     */
    private String wareName;
    /**
     * 租户编码
     */
    private String tenantId;

    private String category;


    private String teachingMode;
}
