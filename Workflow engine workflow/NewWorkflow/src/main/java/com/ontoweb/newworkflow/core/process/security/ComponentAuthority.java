package com.ontoweb.newworkflow.core.process.security;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/29 下午2:45
 * @description：
 * @modified By：
 * @version: $
 */
@Data
public class ComponentAuthority implements Serializable {
    private static final long serialVersionUID = -6363638600031994289L;
    private String component;
    private Authority authority;
    public ComponentAuthority(String component,Authority authority){
        this.component=component;
        this.authority=authority;
    }
}
