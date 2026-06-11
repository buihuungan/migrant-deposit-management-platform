package com.wust.businessback.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;


/**
 * feign调用服务时，会丢失请求头信息。需要在这里把认证信息收到添加上去
 * /**
 *
 * @author wanheng
 * @date 2020/8/10 10:30
 */
@Configuration
@Slf4j
public class FeignTokenInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        log.info("======上下文中获取原请求信息======");
        String token = "without token";
        String userId = "without user";
        String tenantId = "without tanant";
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headValue = request.getHeader(headerName);
            log.info("headValue:{}" + headValue);
            log.info("===原请求头信息=== headName: {}, headValue: {}", headerName, headValue);
            if ("x-access-token".equals(headerName) || "x-access-token".equals(headerName)) {
                token = headValue;
            }
            if ("userId".equals(headerName) || "userid".equals(headerName))
                userId = headValue;
            if ("tenant_id".equals(headerName))
                tenantId = headValue;
        }

        log.info("=======Feign添加头部信息start======");
        //上面的是原请求的头部信息，在转发的时候需要feign接口需要header时再这加入头部信息
        requestTemplate.header("X-Access-Token", token);
        requestTemplate.header("userId", userId);
        requestTemplate.header("tenant_id", tenantId);
        log.info("=======Feign添加头部信息end======");
    }
}

