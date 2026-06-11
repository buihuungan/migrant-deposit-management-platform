package cn.zhenglili.fmaking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName：SwaggerConfig
 * @Description：TODO
 * @Author：zhenglili
 * @Date：2020/7/23 20:04
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private String basePackage = "cn.zhenglili.fmaking.desform.controller";
    private String title = "swagger表单设计器 API文档";
    private String descriltion = "";
    private String version = "1.0";

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))//扫描的注解文件夹
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(){
        // 设置文档基本属性
        return new ApiInfoBuilder()
                .title(title)
                .version(version)
                .description(descriltion)
                .build();
    }



}
