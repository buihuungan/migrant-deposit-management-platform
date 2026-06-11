package com.ontoweb.newworkflow.margin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MainPayment implements Serializable {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    //创建时间
    private Date createTime;

    //项目地址
    private String projectAddress;
    //详细地址
    private String addressDetail;
    //企业名称
    private String enterpriseName;
    //项目名称
    private String projectName;
    //合同金额
    private Double contractAmount;
    //存缴比例
    private String proportions;
    //负责人
    private String responsiblePerson;
    //联系方式
    private String mobile;
    //保证金金额
    private Double money;
    //剩余保证金金额
    private Double remainingAmount;
    //统一社会信用代码
    private String creditCode;
    //邮编
    private String postalCode;
    //企业通信地址
    private String companyAddress;
    //差异化存缴条件
    private String reason;
    //申请的起始日期
    private Date formCreateDate;
    //申请的结束日期
    private Date formEndDate;
    //存缴方式
    private String depositWay;
    //存储开始日期
    private Date storageStartDate;
    //存储结束日期
    private Date storageEndDate;
}
