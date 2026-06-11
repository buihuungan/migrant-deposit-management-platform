package com.ontoweb.newworkflow.core.model;

import io.swagger.models.auth.In;
import lombok.Data;
import org.springframework.util.SerializationUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/22 下午10:04
 * @description：
 * <p>
 *     保存流程文件，格式为json格式
 *     原有的系统中没有这个表
 * </p>
 * @modified By：
 * @version: $
 */
@Data
@Entity
@Table(name = "UFLO_BLOB_TEMP")
public class BlobTemp {
    @Id
    @Column(name="ID_")
    private Long id;

    @Column(name="NAME_",length=60)
    private String name;

    @Lob
    @Column(name="BLOB_VALUE_",length=1024000)
    private byte[] blobValue;

    @Column(name="CREATE_TIME_",length=1024000)
    private String createTime;

    @Column(name="DESCRIPTION_",length=1024000)
    private String desc;


    public BlobTemp(){}
    public BlobTemp(Object obj){
        byte[] b= SerializationUtils.serialize(obj);
        setBlobValue(b);
    }
}
