package com.base.sys.auth;

import com.base.api.exception.DaoException;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.SysConstant;
import com.base.sys.api.dto.AuthenticationUserDto;
import com.base.sys.api.entity.TenantManageEntity;
import com.base.sys.async.service.AsyncCacheService;
import com.base.sys.async.service.impl.AsyncCacheServiceImpl;
import com.base.sys.auth.constant.TokenConstant;
import com.base.sys.auth.token.util.JwtUtil;
import com.base.sys.config.SysSecurityConfig;
import com.base.sys.tenant.dao.TenantManageDao;
import com.base.sys.tenant.dao.impl.TenantManageDaoImpl;
import com.base.sys.utils.JacksonUtil;
import com.base.sys.utils.SpringUtilEx;
import com.base.sys.utils.ThreadLocalUtil;
import com.base.sys.utils.VerCodeImgUtil;
import com.dap.dmc.client.entity.ThirdUserInfoVO;
import com.dap.dmc.client.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

public class PreUsernamePasswordAuthenticationFilter extends GenericFilterBean {
    protected final Logger logger = LoggerFactory.getLogger(PreUsernamePasswordAuthenticationFilter.class);
    private RequestMatcher requiresAuthenticationRequestMatcher;
    public static ThreadLocal<String> urlThreadLocal = new InheritableThreadLocal<>();
    private SysUserDetailsService sysUserDetailsSave = SpringUtilEx.getBean(SysUserDetailsService.class);
    private AuthService authService;
    private UserDetailsService userDetailsService;

    public PreUsernamePasswordAuthenticationFilter(String loginProcessingUrl, UserDetailsService userDetails, AuthService authService) {
        this.userDetailsService = userDetails;
        this.authService = authService;
        this.requiresAuthenticationRequestMatcher = new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        logger.info("doFilter :{} 请求地址 :{}", httpServletRequest.getMethod(), httpServletRequest.getRequestURI());
        String username = "";
        String url = "";
        String method = "";
        String password = "";
        String tenantId = null;
        ParameterRequestWrapper wrapper = new ParameterRequestWrapper(httpServletRequest);
        TenantManageDao tenantManageDao = SpringUtilEx.getBean(TenantManageDaoImpl.class);
        AuthenticationUserDto authenticationUserDTo = new AuthenticationUserDto();
        String authorization = httpServletRequest.getHeader("Authorization");
        String tenantCode = httpServletRequest.getHeader("tenantCode");

        Claims claims = JwtUtil.parseJWT(JwtUtil.getToken(authorization));
        if (requiresAuthenticationRequestMatcher.matches(httpServletRequest)) {
            //对接数字平台后传过来的token
            if (StringUtils.isNotEmpty(authorization)&& null == claims) {
                //对接数字平台后的过滤逻辑
                ThirdUserInfoVO thirdUserInfoVO = authService.checkToken(authorization);
                if (Objects.isNull(thirdUserInfoVO)) {
                    logger.warn("user is null!");
                    return;
                }
                if (StringUtils.isNotEmpty(tenantCode)) {
                    tenantId = tenantManageDao.getIdByCodeOnLogin(tenantCode);
                    if (StringUtils.isEmpty(tenantId)) {
                        tenantId = sysUserDetailsSave.saveTenantInfo(tenantCode);
                    }
                    /*同步用户和租户信息*/
                    sysUserDetailsSave.saveUserInfo(thirdUserInfoVO, tenantId);
                }
                if (StringUtils.isEmpty(tenantId)) {
                    logger.error("租户不存在");
                    throw new RuntimeException("租户信息不存在【" + tenantCode + "】");
                }
                username = thirdUserInfoVO.getUsername();
                password = SysSecurityConfig.DEFAULT_TOKEN_PASSWORD;
            } else {
                //系统登录
                Map<String, String> map = new HashMap<>();
                try {
                    map = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                } catch (Exception e) {
                }
                //增加对验证码的校验
                String verifyCode = map.get("verifyCode");
                if (VerCodeImgUtil.getVerifyCode(verifyCode) == null) {
                    getRespError(response, 203, "验证码输入错误");
                    return;
                }
                username = map.get(SPRING_SECURITY_FORM_USERNAME_KEY);
                tenantCode = map.get("tenantCode");
                //根据字典编码查询出租户ID,并写入threadLocal,用于登录时的mybatis拦截器使用
                if (StringUtils.isNotEmpty(tenantCode)) {
                    try {
                        tenantId = tenantManageDao.getIdByCodeOnLogin(tenantCode);
                    } catch (DaoException e) {
                        logger.error("filter error", e);
                    }
                }
                if (tenantId == null) {
                    getRespError(response, 201, "该租户不存在");
                    return;
                } else {
                    TenantManageEntity tenantManageEntity = tenantManageDao.getObjectById(tenantId);
                    if (tenantManageEntity.getTenantStatus().equals("0")) {
                        getRespError(response, 201, "该租户已被禁用");
                    }
                    if (tenantManageEntity.getAuthStatus().equals("0")) {
                        getRespError(response, 201, "该租户未授权");
                    }
                }
                password = map.get(SPRING_SECURITY_FORM_PASSWORD_KEY);
            }
            authenticationUserDTo.setTenantId(tenantId);
            authenticationUserDTo.setUserName(username);
            authenticationUserDTo.setPassword(password);
            authenticationUserDTo.setTenantCode(tenantCode);
            authenticationUserDTo.setAuthorization(null == authorization ? "null" : authorization);
            ThreadLocalUtil.put(SysConstant.USER_INFO, authenticationUserDTo);
            ThreadLocalUtil.put(TokenConstant.AUTHORIZATION, authenticationUserDTo.getAuthorization());
            wrapper.setAttribute(SPRING_SECURITY_FORM_USERNAME_KEY, username + "@@_@@" + tenantId + "@@_@@" + authenticationUserDTo.getAuthorization());
            wrapper.setAttribute(SPRING_SECURITY_FORM_PASSWORD_KEY, password);
        }

        if (request instanceof HttpServletRequest) {
            httpServletRequest = (HttpServletRequest) request;
            url = httpServletRequest.getRequestURI();
            method = httpServletRequest.getMethod();
            urlThreadLocal.set(method + url);
            //请求路径
            //当为登录请求时 写入缓存
            if (url.equals("/system/login")) {
                try {
                    AsyncCacheService asyncCacheService = SpringUtilEx.getBean(AsyncCacheServiceImpl.class);
                    asyncCacheService.asyncWriteCache();
                    asyncCacheService.asyncWriteUserCache();
                    asyncCacheService.asyncWriteUserDataRule();
                } catch (Exception e) {
                    logger.error("error", e);
                }
            }
        }
        if (!checkSwaggerUrl(url)) {
            try {
                HttpServletRequest httpRequest = (HttpServletRequest) request;
                if (org.springframework.util.StringUtils.hasText(authorization) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (!ObjectUtils.isEmpty(claims)) {
                        authenticationUserDTo.setUserName(claims.get(TokenConstant.ACCOUNT, String.class));
                        authenticationUserDTo.setTenantId(claims.get(TokenConstant.TENANT_ID, String.class));
                        authenticationUserDTo.setUserId(claims.get(TokenConstant.USER_ID, String.class));
                        authenticationUserDTo.setAuthorization(claims.get(TokenConstant.AUTHORIZATION, String.class));
                        if (null == authenticationUserDTo.getAuthorization()) {
                            authenticationUserDTo.setAuthorization("null");
                        }
                        ThreadLocalUtil.put(SysConstant.USER_INFO, authenticationUserDTo);
                        UserDetails userDetails = userDetailsService.loadUserByUsername(new StringBuilder(authenticationUserDTo.getUserName()).append("@@_@@").append(authenticationUserDTo.getTenantId()).append("@@_@@").append(authenticationUserDTo.getAuthorization()).toString());
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }

                }
            } catch (Exception e) {
                logger.error("error:{}" + url, e);

            } finally {
                ThreadLocalUtil.clear();
            }
            logger.info("filterChain.doFilter");
            filterChain.doFilter(wrapper, response);
        }
    }

    /**
     * 检查当前请求是否为swagger请求
     *
     * @return
     */
    private boolean checkSwaggerUrl(String url) {
        if (url.equals("/") || url.equals("/csrf")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取响应的错误信息
     *
     * @param response
     * @param errorCode
     * @param errorMsg
     */
    private void getRespError(ServletResponse response, Integer errorCode, String errorMsg) {
        try {
            response.setContentType("application/json;charset=utf-8");
            Result result = new Result();
            result.setMsg(errorMsg);
            result.setCode(errorCode);
            PrintWriter out = response.getWriter();
            out.write(JacksonUtil.obj2json(result));
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("获取相应异常失败！", e);
        }
    }
}
