package com.base.sys.log.service.impl;

import com.base.api.exception.DaoException;
import com.base.api.util.SnowflakeIdWorker;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.common.Result;
import com.base.sys.api.entity.BaseLogAudit;
import com.base.sys.api.log.Operation;
import com.base.sys.log.service.BaseLogAuditService;
import com.base.sys.utils.JacksonUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author makejava
 * @since 2021-01-14 09:40:19
 */
//@Aspect
@Service("baseLogAuditService")
public class BaseLogAuditServiceImpl extends AbstractBaseServiceImpl<BaseLogAudit, String> implements BaseLogAuditService {
    //定义切点 @Pointcut
    //在注解的位置切入代码
    @Pointcut("@annotation(com.base.sys.api.log.Operation)")
    public void logPoinCut() {
        //
    }
    //切面 配置通知
    @AfterReturning(returning="rvt",pointcut="logPoinCut()")
    public void saveSysLog(JoinPoint joinPoint, Result rvt) {
        //保存日志
        BaseLogAudit auditLog = new BaseLogAudit();

        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();
        //获取操作
        Operation operation = method.getAnnotation(Operation.class);
        if (operation != null) {
            String buttonName = operation.buttonName();
            String menuName = operation.menuName();
            String desc = operation.descr();
            auditLog.setButtonName(buttonName);
            auditLog.setMenuName(menuName);
            auditLog.setDesc(desc);
        }
        //获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        //获取请求的方法名
        String methodName = method.getName();
        auditLog.setMethonName(className + "." + methodName);
        //请求的参数
        Object[] args = joinPoint.getArgs();
        //将参数所在的数组转换成json
        String params = null;
        try {
            params = JacksonUtil.obj2json(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        auditLog.setParams(params);

        //获取用户名
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            auditLog.setOperatorUserName(authentication.getName());
            WebAuthenticationDetails details = (WebAuthenticationDetails)authentication.getDetails();
            auditLog.setOperateIp(details.getRemoteAddress());

        }
        auditLog.setId(SnowflakeIdWorker.getID());
        auditLog.setCreateDate(new Date());
        if(!StringUtils.isEmpty(rvt)){
            if(200==rvt.getCode()){
                auditLog.setOperateResult("成功");
            }else{
                auditLog.setOperateResult("失败");
            }
        }
        //调用service保存SysLog实体类到数据库
        try {
            this.getBaseDao().saveObject(auditLog);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
}
