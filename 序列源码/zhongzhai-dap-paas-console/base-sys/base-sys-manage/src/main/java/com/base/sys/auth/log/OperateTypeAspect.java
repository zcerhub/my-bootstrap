package com.base.sys.auth.log;

import com.base.api.exception.DaoException;
import com.base.api.util.SnowflakeIdWorker;
import com.base.sys.api.dto.AuthenticationUserDto;
import com.base.sys.api.entity.BaseLogAudit;
import com.base.sys.async.service.BaseLogAuditAsyncService;
import com.base.sys.log.dao.BaseLogAuditDao;
import com.base.sys.utils.UserUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Aspect
public class OperateTypeAspect {

    public static final String POST = "POST";
    public static final String DELETE = "DELETE";
    public static final String PUT = "PUT";
    public static final String GET = "GET";

    //组件类型
    public static final String BASE = "0";
    public static final String CACHE = "1";
    public static final String CONFIG = "2";
    public static final String GOV = "3";
    public static final String MQ = "4";
    public static final String REGISTER = "5";
    public static final String SEQ = "6";
    @Value("${gientech.route.audit-log.enabled:false}")
    private boolean synchLog;

    @Autowired
    private BaseLogAuditDao baseLogAuditDao;
    @Autowired
    BaseLogAuditAsyncService operateLogAsyncService;

    @Pointcut("@annotation(com.base.sys.auth.log.AuditLog)")
    private void pointcut() {}

    @AfterReturning("pointcut() && @annotation(logger)")
    public void beforeAdvice(JoinPoint joinPoint, AuditLog logger) {
        BaseLogAudit sai = getBaseLogAudit(joinPoint, logger);
        sai.setOperateResult("成功");
        try {
            baseLogAuditDao.saveObject(sai);
            if(synchLog){operateLogAsyncService.asyncLogAudit(sai,sai.getComponentType());}
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    private BaseLogAudit getBaseLogAudit(JoinPoint joinPoint, AuditLog logger) {
        BaseLogAudit sai = new BaseLogAudit();
        AuthenticationUserDto user = UserUtil.getUser();
        //添加类方法
        sai.setButtonName(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        sai.setMethonName(logger.operateType());
        sai.setComponentType(logger.componentType());
        sai.setId(SnowflakeIdWorker.getID());
        sai.setCreateUserId(user.getUserId());
        sai.setMenuName(logger.modelName());
        sai.setOperateIp(user.getUserIp());
        sai.setTenantId(user.getTenantId());
        sai.setOperatorUserName(user.getUserName());
        sai.setCreateDate(new Date());
        return sai;
    }

    @AfterThrowing("pointcut() && @annotation(logger)")
    public void advice(JoinPoint joinPoint, AuditLog logger) {
        BaseLogAudit sai = getBaseLogAudit(joinPoint, logger);
        sai.setOperateResult("失败");
        try {
            baseLogAuditDao.saveObject(sai);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
}
