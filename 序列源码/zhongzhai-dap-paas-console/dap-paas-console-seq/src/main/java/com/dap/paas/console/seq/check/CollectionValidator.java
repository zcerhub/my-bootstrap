package com.dap.paas.console.seq.check;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * @className LegalValidator
 * @description 集合元素合法校验
 * @author renle
 * @date 2023/12/05 18:14:39 
 * @version: V23.06
 */
public class CollectionValidator implements ConstraintValidator<CollectionValue, Collection<String>> {

    String regex = "^\\d{1,32}$";

    Pattern pattern = Pattern.compile(regex);

    @Override
    public void initialize(CollectionValue constraintAnnotation) {
        regex = constraintAnnotation.regex();
        if (StringUtils.isNotBlank(regex)) {
            pattern = Pattern.compile(regex);
        }
    }

    /**
     * 判断是否校验成功
     * @param values 要校验的值
     */
    @Override
    public boolean isValid(Collection<String> values, ConstraintValidatorContext context) {
        return values.stream().allMatch(value -> pattern.matcher(value).matches());
    }
}
