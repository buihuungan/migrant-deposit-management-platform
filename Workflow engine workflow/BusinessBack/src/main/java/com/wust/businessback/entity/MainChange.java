package com.wust.businessback.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MainChange implements Serializable {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    //关联表id
    private String mainId;
    //创建时间
    private Date createTime;


    //原存缴方式
    private String oldDepositMethod;
    //新存缴方式
    private String newDepositMethod;
    //变更原因
    private String changeReason;
    //办理方式
    //0：自行办理
    //2：经纪公司代办（免费）
    private Integer handlingMethod;
}
