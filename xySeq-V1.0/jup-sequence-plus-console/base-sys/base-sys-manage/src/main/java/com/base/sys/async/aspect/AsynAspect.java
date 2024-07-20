package com.base.sys.async.aspect;

import com.base.sys.async.service.AsyncCacheService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AsynAspect {

    @Lazy
    @Autowired
    private AsyncCacheService asyncCacheService;

    /**
     * 通过注解的方式加入切入点
     **/
    @Pointcut("@annotation(com.base.sys.async.aspect.AsynSelectMenuAnnotation)")
    public void logPointCut() {
        System.out.println("初始化注解切入点...");
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        //执行方法
        Object method = point.proceed();
        /**  刷缓存**/
        try{
            asyncCacheService.asyncWriteUserDataRule();
        }catch (Exception e){
           throw  e;
        }
        return method;
    }




}
