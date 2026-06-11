package com.ontoweb.newworkflow.core.model;

import lombok.Data;
import org.springframework.util.SerializationUtils;

import javax.persistence.*;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 上午11:10
 * @description：历史流程设计存储
 * @modified By：
 * @version: 1.0$
 */
@Entity
@Table(name="UFLO_HIS_BLOB")
@Data
public class HistoryBlob {
    @Id
    @Column(name="ID_")
    private long id;

    @Lob
    @Column(name="BLOB_VALUE_",length=1024000)
    private byte[] blobValue;

    public HistoryBlob(){}
    public HistoryBlob(Object obj){
        byte[] b= SerializationUtils.serialize(obj);
        setBlobValue(b);
    }
}
