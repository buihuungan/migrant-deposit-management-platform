package com.ontoweb.newworkflow.core.model;

import com.ontoweb.newworkflow.core.env.Context;
import com.ontoweb.newworkflow.core.model.variable.VariableType;
import lombok.Data;
import org.hibernate.Session;
import org.springframework.util.SerializationUtils;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/27 上午11:13
 * @description：历史变量
 * @modified By：
 * @version: $
 */
@Entity
@Table(name="UFLO_HIS_VARIABLE")
@Data
public class HistoryVariable {
    @Id
    @Column(name="ID_")
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name="TYPE_",length=15)
    private VariableType type;

    @Column(name="HIS_PROCESS_INSTANCE_ID_")
    private long historyProcessInstanceId;

    @Column(name="KEY_",length=60)
    private String key;

    @Column(name="VALUE_")
    private String stringValue;

    @Transient
    private Object variableObject;

    public Object getVariableObject(){
        return variableObject;
    }

    public void init(Context context){
        SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        Session session=context.getSession();
        if(type.equals(VariableType.Blob) || type.equals(VariableType.Text)){
            HistoryBlob blob=(HistoryBlob)session.get(HistoryBlob.class,Long.valueOf(stringValue));
            variableObject= SerializationUtils.deserialize(blob.getBlobValue());
        }else if(type.equals(VariableType.String)){
            variableObject=stringValue;
        }else if(type.equals(VariableType.Date)){
            try {
                variableObject=sd.parse(stringValue);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }else if(type.equals(VariableType.Integer)){
            variableObject=Integer.valueOf(stringValue);
        }else if(type.equals(VariableType.Character)){
            variableObject=Character.valueOf((char)(stringValue.getBytes()[0]));
        }else if(type.equals(VariableType.Double)){
            variableObject=Double.valueOf(stringValue);
        }else if(type.equals(VariableType.Float)){
            variableObject=Float.valueOf(stringValue);
        }else if(type.equals(VariableType.Long)){
            variableObject=Long.valueOf(stringValue);
        }else if(type.equals(VariableType.Byte)){
            variableObject=Byte.valueOf(stringValue);
        }else if(type.equals(VariableType.Short)){
            variableObject=Short.valueOf(stringValue);
        }else if(type.equals(VariableType.Boolean)){
            variableObject=Boolean.valueOf(stringValue);
        }
    }
}
