package cn.zhenglili.fmaking.desform.entity;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName：DesForm
 * @Description：TODO
 * @Author：zhenglili
 * @Date：2020/7/29 15:46
 **/
@Data
public class DesForm {

    /**
     * 主键
     */
    private String id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 内容
     */
    private String content;

    /**
     * 编码
     */
    private String encoding;

    /**
     * 图标
     */
    private String icon;

    /**
     * userId
     */
    private String userId;

    private Date createTime;

    private Date updateTime;

    private int deleteFlag;

    private String tenantId;

    public DesForm() {
    }

    public DesForm(String name, String encoding, String icon, String userId, String tenantId) {
        this.name = name;
        this.encoding = encoding;
        this.icon = icon;
        this.userId = userId;
        this.tenantId = tenantId;
    }

    public DesForm(String name, String encoding, String icon, String userId) {
        this.name = name;
        this.encoding = encoding;
        this.icon = icon;
        this.userId = userId;
    }
}
