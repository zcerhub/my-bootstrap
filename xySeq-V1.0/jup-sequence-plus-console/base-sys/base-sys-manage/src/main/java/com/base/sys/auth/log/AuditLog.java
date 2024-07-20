package com.base.sys.auth.log;

import java.lang.annotation.*;


/**
 * 审计日志自定义标签
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuditLog {
    // 模块名称
    String modelName();
    //操作类型
    String operateType();
    // 组件类型类型
    String componentType();

}
