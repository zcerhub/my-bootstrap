package ctbiyi.com.config;

import ctbiyi.com.security.BiyiUserServiceManage;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class FeignConfiguration {

    //如果该服务作为子应用运行时需要打开该注解，并根据实际情况修改配置文件对应位置feign调用地址
    //@Value("${biyi.token.user-service-url}")
    private String userServiceUrl;

    /**
     * 创建用户权限信息feign调用服务
     *
     * @return 用户权限信息feign调用服务对象
     */
    @Bean
    public BiyiUserServiceManage userServiceManage() {
        return Feign.builder().encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .client(new OkHttpClient())
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                //.errorDecoder(new ExceptionErrorDecoder())
                .target(BiyiUserServiceManage.class, userServiceUrl);
    }
}
