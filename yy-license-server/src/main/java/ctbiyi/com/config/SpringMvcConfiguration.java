package ctbiyi.com.config;

import com.ctbiyi.captcha.interceptor.CaptchaHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMvcConfiguration implements WebMvcConfigurer {

    /**
     * 组件验证码拦截
     *
     * @return CaptchaHandlerInterceptor
     */
    @Bean
    public CaptchaHandlerInterceptor captchaHandlerInterceptor() {
        return new CaptchaHandlerInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(captchaHandlerInterceptor());

    }

}
