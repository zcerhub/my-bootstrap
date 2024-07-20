package com.base.sys.config;

import com.base.sys.auth.PreUsernamePasswordAuthenticationFilter;
import com.base.sys.auth.SysAccessDecisionManager;
import com.base.sys.auth.SysUserDetailsService;
import com.base.sys.handler.*;
import com.dap.dmc.client.service.AuthService;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SysSecurityConfig extends WebSecurityConfigurerAdapter {
	
	protected final Logger logger = LoggerFactory.getLogger(SysSecurityConfig.class);
	
    public static String loginPage = "/system/login";
    private static String logoutPage = "/system/logout";
    private static String loginKey = "username";
    private static String loginValue = "password";
    
    public static String DEFAULT_TOKEN_PASSWORD = "!@#$DEFAULT_TOKEN_PASSWORD$#@!";
    @Autowired
    private SysUserDetailsService sysUserDetailsService;
    @Autowired
    private AuthService authService;


    @Bean
    @Override
    @SneakyThrows
    public AuthenticationManager authenticationManagerBean() {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/",
                "/index.html",
                "/dist-route/index.html",
                "/dist-route/**",
                "/apiRoute/route-rule-config/**",
                "/paas4-static/**",
                "/favicon.ico",
                "/swagger-ui.html" ,
                "/swagger-resources/**",
                "/webjars/**",
                "/v2/api-docs",
                "/tenantmanage/list",
                "/baseSysUser/getVarCode",//获取验证码
                "/echo",
                "/cache/multiCenterData/download/**",
                "/rocketmq/api/query/route/to/connect",
                "/rocketmq/api/query/unit/to/connect",
                "/v1/basic/sync/**",
                "/v1/seq/publish/insert/sdk",
                "/v1/seq/publish/getStringSeq",
                "/v1/seq/publish/getOptionalSeq",
                "/v1/seq/publish/cancelOptionalSeq",
                "/v1/seq/publish/getStringIps",
                "/v1/config/openApi/publish",
                "/v1/config/openApi/grey",
                "/v1/config/openApi/batch-publish",
                "/v1/config/openApi/queryList",
                "/v1/config/openApi/queryConfig",
                "/v1/config/openApi/config-offline",
                "/v1/config/openApi/grey-offline", //增加灰度下线功能
                "/v1/config/openApi/global-publish",//公共配置下发功能
                "/v1/config/openApi/configCount",//配置统计
                "/v1/config/openApi/config-batch-offline",
                "/v1/config/openApi/delete",
                "/v1/config/openApi/getClientList",
                "/v1/config/openApi/getEnvList",
                "/v1/config/openApi/getClientInstanceResult",
                "/v1/config/openApi/refreshServerNode",
                "/v1/config/detect/do-detect",
                "/v1/console/detect/do-detect",
                "/v1/gov/overview/statistics",//对外暴露的服务治理流控统计接口
                "/baseLogAudit/insert"//对外提供路由组件对接服务治理审计日志
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().
                anyRequest().
                authenticated().
                accessDecisionManager(new SysAccessDecisionManager()).and()
                .addFilterBefore(
                        new PreUsernamePasswordAuthenticationFilter(loginPage,sysUserDetailsService,authService),
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
                accessDeniedHandler(new SysAuthenticationAccessDeniedHandler()).and().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
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
