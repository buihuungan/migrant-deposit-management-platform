package com.ontoweb.newworkflow.core.service.impl;

import com.ontoweb.newworkflow.core.service.RestService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/2 上午10:17
 * @description：
 * @modified By：
 * @version: $
 */
public class RestServiceImpl implements RestService {
    private String baseUrl;
    private HttpHeaders headers;
    private RestTemplate template=new RestTemplate();
    private List<HttpMessageConverter<?>> converters;
    public RestServiceImpl(String username,String password){
        headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return;
        }
        headers.add("uflo.access.username", username);
        headers.add("uflo.access.password", password);
    }

    @Override
    public <T> ResponseEntity<T> post(String uri, Object obj, Class<T> responseClazz){
        if(obj==null){
            obj="blank";
        }
        HttpEntity<Object> entity=null;
        if(headers!=null){
            entity=new HttpEntity<Object>(obj,headers);
        }else{
            entity=new HttpEntity<Object>(obj);
        }
        return template.postForEntity(baseUrl+uri,entity,responseClazz);
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }
    public List<HttpMessageConverter<?>> getConverters() {
        return converters;
    }
    public void setConverters(List<HttpMessageConverter<?>> converters) {
        this.converters = converters;
        template.setMessageConverters(converters);
    }
}
