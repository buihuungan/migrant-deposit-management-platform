package com.ontoweb.newworkflow.core.model;

import lombok.Data;
import org.springframework.util.SerializationUtils;

import javax.persistence.*;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/26 下午3:00
 * @description：将流程图json存入数据库
 * @modified By：
 * @version: 1.0$
 */
@Entity
@Table(name="UFLO_BLOB")
@Data
public class Blob {
    @Id
    @Column(name="ID_")
    private long id;

    @Column(name="NAME_",length=60)
    private String name;

    @Column(name="PROCESS_ID_")
    private long processId;

    @Lob
    @Column(name="BLOB_VALUE_",length=1024000)
    private byte[] blobValue;

    public Blob(){}
    public Blob(Object obj){
        byte[] b= SerializationUtils.serialize(obj);
        setBlobValue(b);
    }
}
