package com.base.sys.api.common;

/**
 * 规范controller接口标准响应构造的泛型使用
 *
 * @author Arlo
 * @date 2021/10/15
 * @see ResultUtils 原响应构造工具
 **/
public class ResultHelper {

    private ResultHelper() {
        // no need
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T object) {
        return build(ResultEnum.SUCCESS, object);
    }


    public static <T> Result<T> error() {
        return error(null);
    }

    public static <T> Result<T> error(T object) {
        return build(ResultEnum.FAILED, object);
    }

    public static <T> Result<T> build(ResultEnum re) {
        return build(re, null);
    }

    public static <T> Result<T> build(ResultEnum re, T object) {
        return build(re.getCode(), re.getMsg(), object);
    }

    public static <T> Result<T> build(Integer code, String message, T object) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(message);
        result.setData(object);
        return result;
    }
}
