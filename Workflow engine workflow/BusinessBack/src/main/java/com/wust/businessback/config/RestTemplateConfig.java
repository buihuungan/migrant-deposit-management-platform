package com.wust.businessback.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    //通过实例bean的方式 将RestTemplate 注入spring 代理
    @Bean
    public RestTemplate restTemplate() {
        // 创建 Apache HttpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 使用 Apache HttpClient 配置请求工厂
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setConnectTimeout(15000);  // 连接超时 15 秒
        factory.setReadTimeout(5000);      // 读取超时 5 秒

        // 返回配置好的 RestTemplate 实例
        return new RestTemplate(factory);
    }
}



