package ctbiyi.com.config;

import ctbiyi.com.swagger.property.BiyiSwaggerProperty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * swagger配置类
 * 如果想对swagger和knife4j设置不能访问，可以使用配置 knife4j.production=true
 * 如果想对swagger和knife4j访问进行权限控制，可以使用配置 knife4j.basic.enable=true，
 *      并使用knife4j.basic.username配置用户名，使用knife4j.basic.password配置密码
 *
 * @author biyi
 */
@Configuration
@EnableConfigurationProperties(BiyiSwaggerProperty.class)
public class SwaggerConfiguration {

    /**
     * swagger配置
     *
     * @param swaggerProperty 配置参数
     * @return swagger的Docket信息
     */
    @Bean
    public Docket docket(BiyiSwaggerProperty swaggerProperty) {
        boolean hasRegex = StringUtils.isNotEmpty(swaggerProperty.getPathRegex());
        boolean hasPath = StringUtils.isNotEmpty(swaggerProperty.getPath());

        Docket docket = new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo(swaggerProperty)).groupName(swaggerProperty.getGroupName())
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperty.getBasePackage())) // 扫描包路径
                // 优先使用pathRegex匹配url路径，
                // pathRegex没有配置的情况下，使用path匹配url路径，
                // 如果pathRegex和path都没有配置的情况下，不过滤任何路径
                .paths(hasRegex
                        ? PathSelectors.regex(swaggerProperty.getPathRegex())
                        : (hasPath ? PathSelectors.ant(swaggerProperty.getPath()) : PathSelectors.any()))
                .build();

        // 支持同一个路径根据参数不同进行区分
        docket.enableUrlTemplating(swaggerProperty.getEnableUrlTemplating());

        return docket;
    }

    /**
     * 构建 api文档的详细信息
     *
     * @param swaggerProperty swagger配置文件
     * @return ApiInfo
     */
    private ApiInfo apiInfo(BiyiSwaggerProperty swaggerProperty) {
        return new ApiInfoBuilder()
                //页面标题
                .title(swaggerProperty.getTitle())
                //创建人
                .contact(new Contact(swaggerProperty.getContact().getName(), swaggerProperty.getContact().getUrl(),
                        swaggerProperty.getContact().getEmail()))
                //版本号
                .version(swaggerProperty.getVersion())
                //描述
                .description(swaggerProperty.getDescription())
                .build();
    }

    /**
     * 引入actuator，需要使用以下类进行过滤处理
     *
     * @return beanPostProcessor
     */
    @Bean
    public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {

        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
                    customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
                }
                return bean;
            }

            private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(
                    List<T> mappings) {
                List<T> copy = mappings.stream()
                        .filter(mapping -> mapping.getPatternParser() == null)
                        .collect(Collectors.toList());
                mappings.clear();
                mappings.addAll(copy);
            }

            @SuppressWarnings("unchecked")
            private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
                try {
                    Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
                    ReflectionUtils.makeAccessible(field);
                    return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
    }
}
