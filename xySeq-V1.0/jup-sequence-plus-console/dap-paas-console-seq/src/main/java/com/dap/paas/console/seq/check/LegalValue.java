package com.dap.paas.console.seq.check;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @className LegalValue
 * @description 合法值校验
 * @author renle
 * @date 2023/12/05 18:12:26 
 * @version: V23.06
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {LegalValidator.class})
public @interface LegalValue {

    /**
     * 检验失败的提示信息
     *
     * @return String
     */
    String message() default "{javax.validation.constraints.error.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 注解的属性
     *
     * @return String[]
     */
    String[] values() default {};
}
