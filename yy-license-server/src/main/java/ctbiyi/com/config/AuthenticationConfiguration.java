package ctbiyi.com.config;

import com.ctbiyi.auth.cas.BiyiCasAuthenticationProvider;
import com.ctbiyi.auth.service.UserInfoService;
import ctbiyi.com.security.BiyiUserDetailsService;
import com.ctbiyi.token.BiyiTokenFilter;
import com.ctbiyi.token.security.BiyiTokenAuthenticationProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

/**
 * 权限认证相关配置
 *
 * @author biyi
 * @Description:
 */
@Configuration
public class AuthenticationConfiguration {



    /**
     * token过滤器
     *
     * @return token过滤器
     */
    @Bean
    @ConditionalOnMissingBean
    public BiyiTokenFilter biyiTokenFilter() {
        return new BiyiTokenFilter();
    }

    /**
     * 密码加密
     *
     * @return PasswordEncoder
     */
    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * CAS provider
     *
     * @param userDetailsService 用户信息
     * @return DefaultCasAuthenticationProvider
     */
    @Bean
    @ConditionalOnMissingBean
    public BiyiCasAuthenticationProvider biyicasAuthenticationProvider(UserDetailsService userDetailsService) {
        return new BiyiCasAuthenticationProvider(userDetailsService);
    }

    /**
     * biyi 认证provider
     *
     * @return BiyiTokenAuthenticationProvider
     */
    @Bean
    public BiyiTokenAuthenticationProvider biyiTokenAuthenticationProvider() {
        return new BiyiTokenAuthenticationProvider();
    }

    /**
     * 获取用户信息服务
     *
     * @param userInfoService 用户信息服务
     * @return 用户详细信息服务
     */
    @Bean
    public UserDetailsService userDetailsService(UserInfoService userInfoService) {
        return new BiyiUserDetailsService(userInfoService);
    }

    /**
     * 默认provider
     *
     * @param passwordEncoder    密码加密
     * @param userDetailsService 用户信息
     * @return DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder,
                                                               UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

    /**
     * Manager
     *
     * @param userDetailsService              用户信息
     * @param daoAuthenticationProvider       dao provider
     * @param biyicasAuthenticationProvider   CAS provider
     * @param biyiTokenAuthenticationProvider token校验provider
     * @return AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                       DaoAuthenticationProvider daoAuthenticationProvider,
                                                       BiyiCasAuthenticationProvider biyicasAuthenticationProvider,
                                                       BiyiTokenAuthenticationProvider biyiTokenAuthenticationProvider
    ) {
        List<AuthenticationProvider> providers = Arrays
                .asList(daoAuthenticationProvider, biyicasAuthenticationProvider, biyiTokenAuthenticationProvider);
        return new ProviderManager(providers);
    }

}
