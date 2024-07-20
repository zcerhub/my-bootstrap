package com.base.sys.config;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.base.sys.auth.PreUsernamePasswordAuthenticationFilter;
import com.base.sys.auth.SysAccessDecisionManager;
import com.base.sys.auth.SysUserDetailsService;
import com.base.sys.auth.session.CustomInvalidSessionStrategy;
import com.base.sys.handler.SysAuthenticationAccessDeniedHandler;
import com.base.sys.handler.SysAuthenticationEntryPoint;
import com.base.sys.handler.SysAuthenticationFailureHandler;
import com.base.sys.handler.SysAuthenticationSuccessHandler;
import com.base.sys.handler.SysLogoutSuccessHandler;

@Configuration
public class SysSecurityConfig extends WebSecurityConfigurerAdapter {
	
	protected final Logger logger = LoggerFactory.getLogger(SysSecurityConfig.class);
	
    private static String loginPage = "/system/login";
    private static String logoutPage = "/system/logout";
    private static String loginKey = "username";
    private static String loginValue = "password";
    
    public static String DEFAULT_TOKEN_PASSWORD = "!@#$DEFAULT_TOKEN_PASSWORD$#@!";
    @Autowired
    private SysUserDetailsService sysUserDetailsService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/",
                "/index.html",
                "/static/**",
                "/paas4-static/**",
                "/favicon.ico",
                "/swagger-ui.html" ,
                "/swagger-resources/**",
                "/webjars/**",
                "/v2/api-docs",
                "/tenantmanage/list",
                "/echo",
                "/cache/multiCenterData/download/**",
                "/rocketmq/api/query/route/to/connect",
                "/rocketmq/api/query/unit/to/connect",
                "/v1/basic/sync/**",
                "/v1/seq/design/pushRules",
                "/v1/seq/publish/insert/sdk",
                "/v1/seq/publish/getStringSeq",
                "/v1/seq/publish/getOptionalSeq",
                "/v1/seq/publish/cancelOptionalSeq",
                "/v1/seq/publish/getStringIps",
                "/v1/seq/server/**",
                "/v1/config/openApi/publish",
                "/v1/config/openApi/grey",
                "/v1/config/openApi/batch-publish",
                "/v1/config/openApi/queryList",
                "/v1/config/openApi/queryConfig",
                "/v1/config/openApi/config-offline",
                "/v1/config/openApi/config-batch-offline",
                "/v1/config/openApi/delete",
                "/v1/config/openApi/getClientList",
                "/v1/config/openApi/getEnvList",
                "/v1/config/openApi/getClientInstanceResult",
                "/v1/config/detect/do-detect",
                "/v1/config/detect/do-detect",
                "/v1/console/detect/do-detect"
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().
                anyRequest().
                authenticated().
                accessDecisionManager(new SysAccessDecisionManager()).and()
                .addFilterBefore(
                        new PreUsernamePasswordAuthenticationFilter(loginPage),
                        UsernamePasswordAuthenticationFilter.class
                ).formLogin().
                loginProcessingUrl(loginPage).
                usernameParameter(loginKey).
                passwordParameter(loginValue).
                permitAll().
                failureHandler(new SysAuthenticationFailureHandler()).
                successHandler(new SysAuthenticationSuccessHandler()).
                and().logout().logoutUrl(logoutPage).
                logoutSuccessHandler(new SysLogoutSuccessHandler()).
                and().logout().permitAll().and().csrf().disable().
                exceptionHandling().
                authenticationEntryPoint(new SysAuthenticationEntryPoint()).
                accessDeniedHandler(new SysAuthenticationAccessDeniedHandler())
                //session失效后
                .and().sessionManagement().invalidSessionStrategy(new CustomInvalidSessionStrategy());
    }


    /**
     * 设置密码加密方式
     **/
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(sysUserDetailsService).passwordEncoder(new CustomPasswordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }
    
    public class CustomPasswordEncoder extends BCryptPasswordEncoder {
    	 
        @Override
        public boolean matches(CharSequence charSequence, String s) {
        	String reqPwd = charSequence.toString();
        	
        	if(StringUtils.equals(reqPwd, DEFAULT_TOKEN_PASSWORD)) {
        		logger.info("token默认密码 放行");
        		return true;
        	}
            return super.matches(charSequence, s);
        }
    }


}
