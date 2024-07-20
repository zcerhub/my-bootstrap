package com.dap.paas.console.seq.check;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.expression.BeanFactoryAccessor;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @className MultiFieldValidator
 * @description 组合校验
 * @author renle
 * @date 2023/12/08 15:44:51 
 * @version: V23.06
 */
public class MultiFieldAssociationValidator implements ConstraintValidator<MultiFieldAssociation, Object> {

    private static final String SPEL_TEMPLATE = "%s%s%s";

    private static final String SPEL_PREFIX = "#{";

    private static final String SPEL_SUFFIX = "}";

    private String when;

    private String must;

    private String field;

    @Override
    public void initialize(MultiFieldAssociation constraintAnnotation) {
        this.when = constraintAnnotation.when();
        this.must = constraintAnnotation.must();
        this.field = constraintAnnotation.field();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(when) || StringUtils.isBlank(must)) {
            return true;
        }
        Map<String, Object> spelMap = getSpelMap(value);

        // when属性是一个spel表达式，执行这个表达式可以得到一个boolean值满足条件
        String whenIsTrue = parseSpel(String.format(SPEL_TEMPLATE, SPEL_PREFIX, when, SPEL_SUFFIX), spelMap);
        if (Boolean.parseBoolean(whenIsTrue)) {
            // 判断must是否满足条件
            String mastIsTrue = parseSpel(String.format(SPEL_TEMPLATE, SPEL_PREFIX, must, SPEL_SUFFIX), spelMap);
            if (!Boolean.parseBoolean(mastIsTrue)) {
                // 获取注解中的message属性值
                String message = context.getDefaultConstraintMessageTemplate();
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message).addPropertyNode(field).addConstraintViolation();
                return false;
            }
        }
        return true;
    }


    @SneakyThrows
    private Map<String, Object> getSpelMap(Object value) {
        Field[] declaredFields = value.getClass().getDeclaredFields();
        Map<String, Object> spelMap = new HashMap<>();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            // 将对象中的属性名和属性值放入map中
            spelMap.put(declaredField.getName(), declaredField.get(value));
        }
        return spelMap;
    }

    /**
     * 解析表达式
     *
     * @param spel spel
     * @param map map
     * @return String
     */
    public static String parseSpel(String spel, Map<String, Object> map) {
        return Optional.ofNullable(spel).map(sl -> {
            ExpressionParser parser = new SpelExpressionParser();
            StandardEvaluationContext context = new StandardEvaluationContext();
            context.setVariables(map);
            context.addPropertyAccessor(new MapAccessor());
            context.addPropertyAccessor(new BeanFactoryAccessor());
            return parser.parseExpression(sl, new TemplateParserContext()).getValue(context, String.class);
        }).orElse("false");
    }
}
