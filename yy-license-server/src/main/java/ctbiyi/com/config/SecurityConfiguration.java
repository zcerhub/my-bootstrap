package ctbiyi.com.config;
import com.ctbiyi.auth.constant.PermissionConstant;
import com.ctbiyi.token.BiyiTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SpringSecurity配置类
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    BiyiTokenFilter biyiTokenFilter;


    AuthenticationManager authenticationManager;

    public SecurityConfiguration(BiyiTokenFilter biyiTokenFilter,
                                 AuthenticationManager authenticationManager) {
        this.biyiTokenFilter = biyiTokenFilter;
        this.authenticationManager = authenticationManager;
    }

    /**
     * SpringSecurity核心逻辑，配置路径对应的权限
     *
     * @param http HttpSecurity对象，SpringSecurity核心配置类
     * @return SpringSecurity过滤器链
     * @throws Exception 配置过程中引发的异常
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .cors().and()
                /*session 策略 如果是用token登录的话策略设置为stateless 不保存session状态*/
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationManager(authenticationManager)
                .authorizeRequests(authorize -> authorize
                        .mvcMatchers(HttpMethod.GET, "/api/user/users?account=current").authenticated()
                        .mvcMatchers(HttpMethod.GET, "/api/roles/**").hasAuthority(PermissionConstant.ROLE_ROLELIST)
                        .mvcMatchers(HttpMethod.GET, "/api/user-permissions?account=current").authenticated()
                        .mvcMatchers(HttpMethod.GET, "/api/user-details?account=current").authenticated()
                        .mvcMatchers(HttpMethod.PUT, "/api/user-details?account=current").authenticated()
                        .mvcMatchers(HttpMethod.PUT, "/api/passwords?action=reset").authenticated()
                        .mvcMatchers(HttpMethod.GET, "api/users").hasAuthority(PermissionConstant.USER_USERLIST)
                        /*  .regexMatchers("api/users.*account=current.*").authenticated()*/
                        .mvcMatchers(HttpMethod.GET, "/api/user-role/roles/**")
                        .hasAuthority(PermissionConstant.USER_USERLIST)
                        .mvcMatchers(HttpMethod.GET, "/api/user-roles/all-roles/**")
                        .hasAuthority(PermissionConstant.USER_USERLIST)
                        .regexMatchers("/api/orgs.*structure=tree.*").hasAuthority(PermissionConstant.USER_USERLIST)
                        .mvcMatchers(HttpMethod.GET, "/api/users/**").authenticated()
                        .mvcMatchers(HttpMethod.POST, "/api/users").hasAuthority(PermissionConstant.USER_USERLIST_ADD)
                        .mvcMatchers(HttpMethod.PUT, "/api/users/**")
                        .hasAuthority(PermissionConstant.USER_USERLIST_UPDATE)
                        .mvcMatchers(HttpMethod.PUT, "/api/users?action=apply")
                        .hasAuthority(PermissionConstant.USER_USERLIST_APPLY)
                        .mvcMatchers(HttpMethod.PUT, "/api/passwords?action=modify")
                        .hasAuthority(PermissionConstant.USER_USERLIST_PWD)
                        .mvcMatchers(HttpMethod.PUT, "/api/user-statuses")
                        .hasAuthority(PermissionConstant.USER_USERLIST_STATUS)
                        .regexMatchers("/api/orgs.*account=current.*").hasAuthority(PermissionConstant.ORG_ORGLIST_TREE)
                        .mvcMatchers(HttpMethod.GET,"/api/users?account=current**").authenticated()
                        .mvcMatchers(HttpMethod.GET, "/api/roles").hasAuthority(PermissionConstant.ROLE_ROLELIST)
                        .mvcMatchers(HttpMethod.POST, "/api/roles").hasAuthority(PermissionConstant.ROLE_ROLELIST_ADD)
                        .mvcMatchers(HttpMethod.PUT, "/api/roles/**")
                        .hasAuthority(PermissionConstant.ROLE_ROLELIST_UPDATE)
                        .mvcMatchers(HttpMethod.DELETE, "/api/roles/**")
                        .hasAuthority(PermissionConstant.ROLE_ROLELIST_DELETE)
                        .mvcMatchers(HttpMethod.PUT, "/api/role-statuses")
                        .hasAuthority(PermissionConstant.ROLE_ROLELIST_DISABLE)

                        .mvcMatchers(HttpMethod.GET, "/api/user-roles/roles")
                        .hasAuthority(PermissionConstant.ROLE_ROLELIST_FINDBYPERM)
                        .mvcMatchers(HttpMethod.PUT, "/api/user-roles/roles/**")
                        .hasAuthority(PermissionConstant.ROLE_ROLELIST_FINDBYPERM)
                        .mvcMatchers(HttpMethod.GET, "/api/user-roles/all-roles")
                        .hasAuthority(PermissionConstant.ROLE_ROLELIST_FINDBYPERM)
                        .mvcMatchers(HttpMethod.POST, "/api/user-roles/users")
                        .hasAuthority(PermissionConstant.ROLE_ROLELIST_FINDBYPERM)
                        .mvcMatchers(HttpMethod.DELETE, "/api/user-roles/users")
                        .hasAuthority(PermissionConstant.ROLE_ROLELIST_FINDBYPERM)
                        .mvcMatchers(HttpMethod.GET, "/api/user-roles/users")
                        .hasAuthority(PermissionConstant.ROLE_ROLELIST_FINDBYPERM)
                        .mvcMatchers(HttpMethod.GET, "/user-roles/not-in-users")
                        .hasAuthority(PermissionConstant.ROLE_ROLELIST_FINDBYPERM)

                        .mvcMatchers(HttpMethod.GET, "/api/orgs").hasAuthority(PermissionConstant.ORG_ORGLIST_TREE)
                        .mvcMatchers(HttpMethod.DELETE, "/api/orgs/**")
                        .hasAuthority(PermissionConstant.ORG_ORGLIST_DELETE)
                        .mvcMatchers(HttpMethod.POST, "/api/orgs").hasAuthority(PermissionConstant.ORG_ORGLIST_ADD)
                        .mvcMatchers(HttpMethod.PUT, "/api/orgs").hasAuthority(PermissionConstant.ORG_ORGLIST_UPDATE)
                        .mvcMatchers(HttpMethod.GET, "/api/orgs/**").hasAuthority(PermissionConstant.ORG_ORGLIST_TREE)

                        .mvcMatchers(HttpMethod.GET, "/api/permissions").hasAuthority(PermissionConstant.PERM_PERMLIST)
                        .mvcMatchers(HttpMethod.POST, "/api/permissions")
                        .hasAuthority(PermissionConstant.PERM_PERMLIST_ADD)
                        .mvcMatchers(HttpMethod.PUT, "/api/permissions/**")
                        .hasAuthority(PermissionConstant.PERM_PERMLIST_UPDATE)
                        .mvcMatchers(HttpMethod.DELETE, "/api/permissions/**")
                        .hasAuthority(PermissionConstant.PERM_PERMLIST_DELETE)
                        .mvcMatchers(HttpMethod.GET, "/api/permission/**")
                        .hasAuthority(PermissionConstant.PERM_PERMLIST)
                        .mvcMatchers(HttpMethod.GET, "/api/role-permissions/**")
                        .hasAuthority(PermissionConstant.ROLE_ROLELIST)
                        .mvcMatchers(HttpMethod.POST, "/api/role-permissions/**")
                        .hasAuthority(PermissionConstant.ROLE_ROLELIST)
                        .mvcMatchers("/api/**").permitAll()
                )
                .addFilterBefore(biyiTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * SpringSecurity核心逻辑，配置路径对应的权限 可以允多个chain,但需要指定顺序
     *
     * @param http HttpSecurity对象，SpringSecurity核心配置类
     * @return SpringSecurity过滤器链
     * @throws Exception 配置过程中引发的异常
     *
     * @Bean
     * @Order(value = 3)
     * SecurityFilterChain filterChainTwo(HttpSecurity http) throws Exception {
     *     return http
     *            .csrf().disable()
     *            .cors().and().build();
     *     }
     */


    /**
     * 需要忽略的路径
     *
     * @return WebSecurityCustomizer需要忽略的路径
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/images/**",
                        "/js/**", "/webjars/**", "/v3/**", "/swagger-ui/*", "/doc.html/**", "/swagger-resources")
                .mvcMatchers("/api/system/login")
                .mvcMatchers("/inner/**")
                .mvcMatchers("/api/system/loginByCas")
                .mvcMatchers("/api/email-codes")
                .mvcMatchers("/api/verify-types")
                .mvcMatchers("/api/email-verifications")
                .mvcMatchers("/api/system/register")
                .mvcMatchers("/api/captcha/drag-captcha")
                .mvcMatchers("/api/captcha/digital-captcha")
                .mvcMatchers("/api/captcha/email-captcha/**")
                .regexMatchers("/api/passwords.*action=forget.*")
                .regexMatchers("/api/orgs.*action=register.*");
    }

}

