package com.base.sys.auth;

import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import com.base.api.exception.DaoException;
import com.base.api.util.SnowflakeIdWorker;
import com.base.sys.api.cache.DictMapCache;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.SysConstant;
import com.base.sys.api.dto.AuthenticationUserDto;
import com.base.sys.api.entity.BaseLogAudit;
import com.base.sys.api.entity.BaseSysUser;
import com.base.sys.api.entity.TenantManageEntity;
import com.base.sys.async.service.AsyncCacheService;
import com.base.sys.async.service.impl.AsyncCacheServiceImpl;
import com.base.sys.config.SysSecurityConfig;
import com.base.sys.log.service.BaseLogAuditService;
import com.base.sys.org.service.BaseSysUserService;
import com.base.sys.tenant.dao.TenantManageDao;
import com.base.sys.tenant.dao.impl.TenantManageDaoImpl;
import com.base.sys.utils.JacksonUtil;
import com.base.sys.utils.SpringUtilEx;
import com.base.sys.utils.UserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.gientech.autho.client.entity.UserInfoEntity;
//import com.gientech.autho.client.service.AuthService;
//import com.gientech.autho.client.utils.ResponseUtil;
//import com.gientech.autho.common.authenum.AuthoResultEnum;
//import com.gientech.autho.common.entity.R;

public class PreUsernamePasswordAuthenticationFilter extends GenericFilterBean {
    protected final Logger logger = LoggerFactory.getLogger(PreUsernamePasswordAuthenticationFilter.class);

    private RequestMatcher requiresAuthenticationRequestMatcher;
    private static String CENTER_LOGINPAGE = "";
    public static ThreadLocal<String> urlThreadLocal = new InheritableThreadLocal<>();

    public PreUsernamePasswordAuthenticationFilter(String loginProcessingUrl) {
        requiresAuthenticationRequestMatcher = new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

//    private AuthService authService = SpringUtilEx.getBean(AuthService.class);
    private BaseSysUserService baseSysUserService = SpringUtilEx.getBean(BaseSysUserService.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    	/* 根据token从认证中心获取用户信息。 */
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        
      //  logger.info("doFilter this:" + this.hashCode());
      //  logger.info("doFilter currentThread:" + Thread.currentThread().getId());
        logger.info("doFilter :{} 请求地址 :{}", httpServletRequest.getMethod() , httpServletRequest.getRequestURI());
        String userId = "";
        String username = "";
        String url = "";
        String method = "";
        String mappingValue = "";
        String tenantCode = "";
        String logTenantId = " ";
        String authorization = " ";
        String password = "";
        ParameterRequestWrapper wrapper = new ParameterRequestWrapper(httpServletRequest);
        TenantManageDao tenantManageDao = SpringUtilEx.getBean(TenantManageDaoImpl.class);
        AuthenticationUserDto authenticationUserDTo = new AuthenticationUserDto();
        
     //   logger.info("UserUtil.getUser() :{}", UserUtil.getUser());
        if (requiresAuthenticationRequestMatcher.matches(httpServletRequest)) {
        	
        	Map<String, String> map = new HashMap<String, String>();
        	try {
        		map = new ObjectMapper().readValue(request.getInputStream(), Map.class);
			} catch (Exception e) {
			}
            
            String tenantId = null;
            username = map.get(SPRING_SECURITY_FORM_USERNAME_KEY);
            authorization = map.get("token");
            if(StringUtils.isEmpty(authorization)) {
            	authorization = httpServletRequest.getHeader("Authorization");
            }
            logger.info("token {} "+authorization);
            if (StringUtils.isNotEmpty(authorization)) {
//                UserInfoEntity user=new UserInfoEntity();
//                user = getUserInfoEntity(response, authorization, user);
                
//                if(user == null) {
//                	logger.warn("checkToken user is null");
//                	return ;
//                }
                
                //由于每次统一授权送过来的租户ID是变化的，不方便使用 先临时写死，后期正式对接可打开 
                //tenantId = user.getTenantId();
                
                tenantId = getDefaultTenantId();
//                username = user.getUsername();
                BaseSysUser baseSysUser = baseSysUserService.getBaseSysUser(username, tenantId);
//                if(baseSysUser == null) {
////                	baseSysUser = new BaseSysUser();
////                	baseSysUser.setAccount(username);
////                	baseSysUser.setPassword(user.getPassword());
////                	baseSysUser.setTenantId(tenantId);
////                	//baseSysUser.setName(user.getName());
////                	baseSysUser.setId(SnowflakeIdWorker.getID());
////                	baseSysUserService.saveObject(baseSysUser);
//                	logger.warn("PAAS平台没有创建用户：" + username + ",请先在PAAS平台创建此用户");
//                	ResponseUtil.returnJson(response, R.success(AuthoResultEnum.TOKEN_AUTH_FAILED.getCode(), "PAAS平台没有创建用户：" + username + ",请先在PAAS平台创建此用户", CENTER_LOGINPAGE));
//                }
                password = SysSecurityConfig.DEFAULT_TOKEN_PASSWORD;
                authenticationUserDTo.setUserId(baseSysUser.getId());
            } else {
                tenantCode = map.get("tenantCode");
                //根据字典编码查询出租户ID,并写入threadLocal,用于登录时的mybatis拦截器使用
                if (!("").equals(tenantCode)) {
                    try {
                        tenantId = tenantManageDao.getIdByCodeOnLogin(tenantCode);
                    } catch (DaoException e) {
                        logger.error("error", e);
                    }
                }
                if (tenantId == null) {
                    logger.error("租户不存在");
                    try {
                        response.setContentType("application/json;charset=utf-8");
                        Result result = new Result();
                        PrintWriter out = response.getWriter();
                        result.setMsg("租户不存在");
                        result.setCode(201);
                        out.write(JacksonUtil.obj2json(result));
                        out.flush();
                        out.close();
                        
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    } 
                    return;
                } else {
                    try {
                        TenantManageEntity tenantManageEntity = tenantManageDao.getObjectById(tenantId);
                        if (tenantManageEntity.getTenantStatus().equals("0")) {
                            response.setContentType("application/json;charset=utf-8");
                            Result result = new Result();
                            PrintWriter out = response.getWriter();
                            result.setMsg("该租户已被禁用");
                            result.setCode(201);
                            out.write(JacksonUtil.obj2json(result));
                            out.flush();
                            out.close();
                        }
                        if (tenantManageEntity.getAuthStatus().equals("0")) {
                            response.setContentType("application/json;charset=utf-8");
                            Result result = new Result();
                            PrintWriter out = response.getWriter();
                            result.setMsg("该租户未授权");
                            result.setCode(201);
                            out.write(JacksonUtil.obj2json(result));
                            out.flush();
                            out.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                password = map.get(SPRING_SECURITY_FORM_PASSWORD_KEY);
            }
            logTenantId = tenantId;
            authenticationUserDTo.setTenantId(tenantId);
            HttpSession session = ((HttpServletRequest) request).getSession();
            session.setAttribute(SysConstant.USER_INFO, authenticationUserDTo);
            wrapper.setAttribute(SPRING_SECURITY_FORM_USERNAME_KEY, username + "@@_@@" + tenantId);
            wrapper.setAttribute(SPRING_SECURITY_FORM_PASSWORD_KEY, password);
            
        }
        
        Map<String, String> pathMap = DictMapCache.getCacheMap().get(SysConstant.ESB_PATH_MAPPING);
        if (request instanceof HttpServletRequest) {
            httpServletRequest = (HttpServletRequest) request;
            url = httpServletRequest.getRequestURI();

            method = httpServletRequest.getMethod();
            urlThreadLocal.set(method + url);
            //请求路径
            String path = httpServletRequest.getRequestURI();
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
            //用请求方式和路径 作为查询key
            String mappingKey = method + path;
            if (null != pathMap) {
                mappingValue = pathMap.get(mappingKey);
            }

        }
        BaseLogAudit baseLogAudit = new BaseLogAudit();
        if (!checkSwaggerUrl(url)) {

            httpServletRequest = (HttpServletRequest) request;
           // HttpSession session = httpServletRequest.getSession();
            AuthenticationUserDto authenticationUserDto = UserUtil.getUser();
            if(authenticationUserDto != null) {
            	userId = authenticationUserDto.getUserId();
                username = authenticationUserDto.getUserName();
                
                SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null) {
                    WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
                    baseLogAudit.setOperateResult("成功");
                    baseLogAudit.setUpdateUserId(userId);
                    baseLogAudit.setOperatorUserName(username);
                    baseLogAudit.setButtonName(url);
                    if(details != null) {
                    	baseLogAudit.setOperateIp(details.getRemoteAddress());
                    }
                    baseLogAudit.setTenantId(logTenantId);
                }
                if (mappingValue != null) {
                    baseLogAudit.setDesc(mappingValue);
                }
                
                BaseLogAuditService baseLogAuditService = SpringUtilEx.getBean(BaseLogAuditService.class);
                if (null != baseLogAudit ) {
                    baseLogAudit.setId(SnowflakeIdWorker.getID());
                    baseLogAuditService.saveObject(baseLogAudit);
                }
            }
            logger.info("filterChain.doFilter");
            filterChain.doFilter(wrapper, response);
        }
    }

    private String getDefaultTenantId() {
		// TODO Auto-generated method stub
    	return getEnvironment().getProperty("dap.default.tenantId", "391312369558487040");//获取配置文件指定的租户ID
	}
    
    /**
     * 数字底座权限校验
     * @param response
     * @param authorization
     * @param user
     * @return
     * @throws IOException
     */
//    private UserInfoEntity getUserInfoEntity(ServletResponse response, String authorization, UserInfoEntity user) throws IOException {
//        if (StringUtils.isEmpty(CENTER_LOGINPAGE)) {
//            String loginPage = authService.getLoginPage();
//            CENTER_LOGINPAGE = loginPage;
//        }
//        if (StringUtils.isEmpty(authorization)) {
//            ResponseUtil.returnJson(response, R.success(AuthoResultEnum.TOKEN_AUTH_FAILED.getCode(),
//                    AuthoResultEnum.NOT_LOGIN.getMsg(), CENTER_LOGINPAGE));
//        } else {
//
//            /* 根据token从认证中心获取用户信息。 */
//            logger.info("authService.checkToken  entry :{}", authorization);
//            user = authService.checkToken(authorization);
//            logger.info("authService.checkToken   end :{}", authorization);
//            // ***重点***将用户信息添加到httpRequest中。需要根据自己的需求更改。
//            if (null == user) {
//                logger.error("user 解析异常 为null");
//                //验证失败
//                ResponseUtil.returnJson(response, R.success(AuthoResultEnum.TOKEN_AUTH_FAILED.getCode(),
//                        AuthoResultEnum.TOKEN_AUTH_FAILED.getMsg(), CENTER_LOGINPAGE));
//            }
//
//        }
//        return user;
//    }


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
}
