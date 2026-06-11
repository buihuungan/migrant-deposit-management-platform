package com.ontoweb.newworkflow.core.process.node;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/29 下午2:43
 * @description：映射图
 * @modified By：
 * @version: $
 */
@Data
public class Mapping implements Serializable {
    private static final long serialVersionUID = -1347697162337032126L;
    private String key;
    private String label;
}
