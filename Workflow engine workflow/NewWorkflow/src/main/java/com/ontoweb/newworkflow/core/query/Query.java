package com.ontoweb.newworkflow.core.query;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/7/28 上午10:20
 * @description：
 * @modified By：
 * @version: $
 */
public interface Query<T> {
    T list();
    int count();
}
