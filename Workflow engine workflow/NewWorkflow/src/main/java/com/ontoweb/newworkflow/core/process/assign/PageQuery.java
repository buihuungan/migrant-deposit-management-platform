package com.ontoweb.newworkflow.core.process.assign;

import lombok.Data;

import java.util.Collection;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/28 上午10:15
 * @description：翻页类
 * @modified By：
 * @version: $
 */
@Data
public class PageQuery<T> {
    private String id;
    private String name;
    private int pageIndex;
    private int pageSize;
    private boolean tree;
    private Collection<T> result;
    private int recordCount;
    private Entity queryParameter;

    public PageQuery(int pageIndex,int pageSize){
        this.pageIndex=pageIndex;
        this.pageSize=pageSize;
    }
}
