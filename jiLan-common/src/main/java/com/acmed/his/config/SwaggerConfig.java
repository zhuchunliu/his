package com.acmed.his.config;
/**
 *
 * Created by Eric on 2017-05-11.
 */

import com.acmed.his.support.AccessToken;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * Swagger配置类
 *
 * @Author Eric
 * @Version 2017-05-11 17:03
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    private Environment environment;

    @Bean
    public Docket createRestApi() {
        List<Parameter> list = Lists.newArrayList();
        list.add(new ParameterBuilder().name("Authorization").description("token令牌").
                modelRef(new ModelRef("string")).parameterType("header").required(false).
                defaultValue("2NhkcDVnYVl3RshHMLVWeDJjUjV1MzMjNxQTM1ITNyUTMyQUQQ9lUFNVV").build());
        return new Docket(DocumentationType.SWAGGER_2).ignoredParameterTypes(AccessToken.class)
                .globalOperationParameters(list)
                .enable((environment.getActiveProfiles()[0].equalsIgnoreCase("pre")||
                        environment.getActiveProfiles()[0].equalsIgnoreCase("re"))?false:true)
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
