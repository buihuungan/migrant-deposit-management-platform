package com.wust.businessback.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MainUse implements Serializable {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    //关联表id
    private String mainId;
    //创建时间
    private Date createTime;


    //法定代表人
    private String legalRepresentative;
    //法定代表人联系电话
    private String legalMobile;
    //工资保证金担保机构
    private String bank;
    //开户银行联系电话
    private String bankMobile;
    //支付对象姓名 名称
    private String paymentRecipient;
    //身份证号
    private String idNumber;
    //使用原因
    private String useReason;
    //使用金额 （万元）
    private Double useMoneyLow;
    //行政执法文书编号
    private String docNumber;
}
