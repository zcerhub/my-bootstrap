package com.dap.paas.console.common.config;

import com.base.sys.auth.AuthenticationUser;
import com.fasterxml.classmate.TypeResolver;
import org.apache.curator.shaded.com.google.common.collect.Sets;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SwaggerAddtion implements ApiListingScannerPlugin {
    @Override
    public List<ApiDescription> apply(DocumentationContext documentationContext) {
        return new ArrayList<>(
                Arrays.asList(
                        new ApiDescription(
                                "权限认证",
                                "/system/login",  //url
                                "用户登录接口", //描述
                                "用户登录接口",
                                Arrays.asList(
                                        new OperationBuilder(
                                                new CachingOperationNameGenerator())
                                                .method(HttpMethod.POST)//http请求类型
                                                .consumes(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
                                                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
                                                .responseModel(new ModelRef("com.base.sys.api.common.Result"))
                                                .summary("用户登录")
                                                .notes("用户登录")//方法描述
                                                .tags(Sets.newHashSet("用户登录"))//归类标签
                                                .parameters(
                                                        Arrays.asList(
                                                                new ParameterBuilder()
                                                                        .type(new TypeResolver().resolve(AuthenticationUser.class))
                                                                        .name("登录报文")
                                                                        .description("登录报文json")
                                                                        .defaultValue("{\"username\":\"admin\",\"password\":\"123456\"}")
                                                                        .parameterType("body")
                                                                        .modelRef(new ModelRef("com.base.sys.auth.AuthenticationUser"))
                                                                        .required(true)
                                                                        .build())
                                                )
                                                .build()),
                                false)));
    }
 
    @Override
    public boolean supports(DocumentationType documentationType) {
        return DocumentationType.SWAGGER_2.equals(documentationType);
    }
}
