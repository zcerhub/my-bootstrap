package com.dap.paas.console.seq.check;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @className LegalValidator
 * @description 合法值校验
 * @author renle
 * @date 2023/12/05 18:14:39 
 * @version: V23.06
 */
public class LegalValidator implements ConstraintValidator<LegalValue, String> {

    Set<String> set = new HashSet<>();

    @Override
    public void initialize(LegalValue constraintAnnotation) {
        set.addAll(Arrays.asList(constraintAnnotation.values()));
    }

    /**
     * 判断是否校验成功
     * @param value 要校验的值
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果set中包含要校验的值，说明校验通过，否则校验失败
        return value == null || set.contains(value);
    }
}
