package com.dap.paas.console.basic.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import java.util.Map;

/**
 * @Author: scalor
 * @Date: 2021/1/23:10:02
 * @Descricption:
 */
public class JacksonUtil {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    private JacksonUtil() {

    }

    public static ObjectMapper getInstance() {
        return objectMapper;
    }

    /**
     * javaBean、列表数组转换为json字符串
     */
    public static String objToJson(Object obj) throws Exception {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
        return objectMapper.writeValueAsString(obj);
    }
    /**
     * javaBean、列表数组转换为json字符串
     */
    public static String obj2json(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    /**
     * json 转JavaBean
     */

    public static <T> T json2pojo(String jsonString, Class<T> clazz) throws Exception {
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return objectMapper.readValue(jsonString, clazz);
    }

    /**
     * json字符串转换为map
     */
    public static <T> Map<String, Object> json2map(String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.readValue(jsonString, Map.class);
    }
//    public static Map<String, String> jsonTomap(String str_json) {
//        Map<String, String> res = null;
//        try {
//            Gson gson = new Gson();
//            res = gson.fromJson(str_json, new TypeToken<Map<String, String>>() {
//            }.getType());
//        } catch (JsonSyntaxException e) {
//        }
//        return res;
//    }
}
