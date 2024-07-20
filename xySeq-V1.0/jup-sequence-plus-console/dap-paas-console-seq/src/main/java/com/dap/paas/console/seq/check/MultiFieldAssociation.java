package com.dap.paas.console.seq.check;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @className MultiFieldAssociation
 * @description 关联注解
 * @author renle
 * @date 2023/12/08 15:46:34
 * @version: V23.06
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MultiFieldAssociation.List.class)
public @interface MultiFieldAssociation {

    /**
     * 错误信息描述，必填
     */
    String message() default "关联校验失败";

    /**
     * 分组校验
     */
    Class<?>[] groups() default {};

    /**
     * 负载
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 指定校验成员
     */
    String field();

    /**
     * 当什么条件下校验,必须是一个spel表达式
     */
    String when();

    /**
     * 必须满足什么条件,必须是一个spel表达式
     */
    String must();

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        MultiFieldAssociation[] value();
    }
}
