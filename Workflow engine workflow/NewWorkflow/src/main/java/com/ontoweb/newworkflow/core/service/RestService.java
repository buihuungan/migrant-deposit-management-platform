package com.ontoweb.newworkflow.core.service;

import org.springframework.http.ResponseEntity;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/2 上午9:59
 * @description：
 * @modified By：
 * @version: $
 */
public interface RestService {
    <T> ResponseEntity<T> post(String uri, Object obj, Class<T> responseClazz);
    String getBaseUrl();
}
