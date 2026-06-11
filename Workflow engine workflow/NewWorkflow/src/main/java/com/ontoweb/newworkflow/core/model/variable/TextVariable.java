package com.ontoweb.newworkflow.core.model.variable;

import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.Blob;
import com.ontoweb.newworkflow.core.utils.IDGenerator;
import lombok.Data;
import org.hibernate.Session;
import org.springframework.util.SerializationUtils;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 下午4:50
 * @description：
 * @modified By：
 * @version: $
 */
@Data
@Entity
@DiscriminatorValue("Text")
public class TextVariable extends Variable{
    @Column(name="BLOB_ID_")
    private long blobId;

    @Transient
    private String text;

    @Transient
    private Blob blob;

    public TextVariable(){}

    public TextVariable(String value,Context context){
        Blob lob=new Blob(value);
        long id= IDGenerator.getInstance().nextId();
        lob.setId(id);
        setBlobId(id);
        Session session=context.getSession();
        session.save(lob);
    }

    public void initValue(Context context){
        Session session=context.getSession();
        blob=(Blob)session.get(Blob.class,blobId);
        text=(String) SerializationUtils.deserialize(blob.getBlobValue());
    }


    @Override
    public String getValue() {
        return text;
    }

    @Override
    public VariableType getType() {
        return VariableType.Text;
    }

    public void setBlobId(long blobId) {
        this.blobId = blobId;
    }
}
