package com.ontoweb.newworkflow.core.process.assign;

import lombok.Data;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/28 上午10:14
 * @description：实体类
 * @modified By：
 * @version: $
 */
@Data
public class Entity {
    private String id;
    private String name;
    private boolean chosen=true;
    public Entity(String id,String name){
        this.id=id;
        this.name=name;
    }
}
