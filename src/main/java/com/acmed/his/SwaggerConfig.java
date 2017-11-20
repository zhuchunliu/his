package com.acmed.his;
/**
 *
 * Created by Eric on 2017-05-11.
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger配置类
 *
 * @Author Eric
 * @Version 2017-05-11 17:03
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.acmed"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo("服务平台相关接口",//大标题
                "服务平台相关接口",//小标题
                "1.0",//版本
                "",
                "Andrew",//作者
                "苏州极致医疗技术有限公司",//链接显示文字
                ""//网站链接
        );
        return apiInfo;
    }
}
