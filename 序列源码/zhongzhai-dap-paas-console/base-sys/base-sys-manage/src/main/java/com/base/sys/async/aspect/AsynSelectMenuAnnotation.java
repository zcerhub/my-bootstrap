package com.base.sys.async.aspect;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AsynSelectMenuAnnotation {

    /** 模块 **/
    String title() default "";

    /**  动作 **/
    String action() default "";

}
