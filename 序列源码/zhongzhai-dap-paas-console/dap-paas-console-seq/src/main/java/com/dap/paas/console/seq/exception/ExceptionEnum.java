package com.dap.paas.console.seq.exception;

import com.base.api.exception.ServiceException;

/**
 * @className ExceptionEnum
 * @description 异常枚举类
 * @author renle
 * @date 2023/11/24 17:43:54 
 * @version: V23.06
 */
public enum ExceptionEnum implements IBaseError {

    SUCCESS("S2001", "请求成功!"),

    SEQ_RECOVERY_NOT_FOUND("S2002", "没有找到对应的序列！、无法回收对应序号!"),

    SEQ_ENGINE_CACHE_KEY_NOT_FOUND("S2003", "参数引擎缓存key无法找到!"),

    SEQ_NOT_FOUND("S2004", "序列编辑器无法找到!"),

    SEQ_CODE_NOT_FOUND("S2005", "序列参数seqCode为空!"),

    SEQ_FROM_SERVER_EMPTY("S2006", "从服务端获取序列为空!"),

    SEQ_RULE_EMPTY("S2007", "没有定义序列规则!"),

    SEQ_CLUSTER_NOT_FOUND("S2008", "集群服务不存在!"),

    SEQ_NODE_NOT_FOUND("S2009", "没有启动的序列节点!"),

    SEQ_SDK_MONITOR_INFO_EMPTY("S2010", "序列SDK监控信息参数为空!"),

    SEQ_UPDATE_HOLDER_FAILED("S2011", "动态修改Holder缓存失败!"),

    SEQ_ACCESS_KEY_CHECK_FAILED("S2012", "accessKey校验失败!"),

    SEQ_DATA_SOURCE_PARAM_EMPTY("S2013", "数据库参数为空!"),

    SEQ_UPDATE_DB_FILE_EMPTY("S2014", "操作数据库文件为空!"),

    SEQ_SWITCH_DB_FAILED("S2015", "数据库切换失败!"),

    PLACE_DIGITS_LESS_THAN_FIXED("S2016", "参数位数小于固定长度位数!"),

    GET_SEQ_QUEUE_ERROR("S2017", "操作队列出错!"),

    GET_SEQ_OPTIONAL_DB_ERROR("S2018", "自选序列操作数据库失败!"),

    LOCK_KEY_EMPTY("S2019", "加锁key不能为空!"),

    LOCK_NOT_ADD_KEY("S2020", "尚未加锁!"),

    LOCK_NOT_HAVE_THREAD("S2021", "当前线程尚未持有锁!"),

    SEQ_OPTIONAL_RULE_VALID("S2022", "序列规则包含自选规则!"),

    SEQ_PLACE_RULE_VALID("S2023", "序列规则包含占位符规则!"),

    SEQ_OPTIONAL_PARAM_VALID("S2024", "自选序列参数为空!"),

    SEQ_OPTIONAL_SEQ_VALUE_VALID("S2025", "自选序列参数长度大于或者等于自选规则长度!"),

    SEQ_OPTIONAL_RULE_EMPTY("S2026", "当前序列不包含自选功能!"),

    SEQ_OPTIONAL_SERIAL_EMPTY("S2027", "当前序列不包含自选规则!"),

    SEQ_OPTIONAL_SERIAL_SELECTED("S2028", "当前序号已被使用!"),

    SEQ_OPTIONAL_SELECTED_NOT_FOUND("S2029", "当前序号无法找到或者状态异常无法选中!"),

    SEQ_OPTIONAL_SERIAL_CANCEL("S2030", "序号选中时无法回收!"),

    SEQ_NUM_MAXIMIZE("S2031", "序列数字规则达到最大值!"),

    SEQ_RULE_NOT_EXIST("S2032", "序列规则无法找到!"),

    SEQ_OPTIONAL_RULE_MAX_1("S2033", "序列开启自选能力的数字规则只能有一个!"),

    SEQ_OPTIONAL_PLACE_RULE_MAX_1("S2034", "序列开启自选能力的数字规则和占位符规则只能有一个!"),

    BODY_NOT_MATCH("S4400","请求的数据格式不符!"),

    SIGNATURE_NOT_MATCH("S4401","请求的数字签名不匹配!"),

    OPERATE_DB_ERROR("S4402","操作数据库异常!"),

    OPERATE_SERVICE_ERROR("S4403","服务操作异常!"),

    NOT_FOUND("S4404", "未找到该资源!"),

    INTERNAL_SERVER_ERROR("S4500", "服务器内部错误!"),

    SERVER_BUSY("S4503","服务器正忙，请稍后再试!");

    /**
     * 错误码
     */
    private final String resultCode;

    /**
     * 错误描述
     */
    private final String resultMsg;

    ExceptionEnum(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    @Override
    public String getResultCode() {
        return resultCode;
    }

    @Override
    public String getResultMsg() {
        return resultMsg;
    }

    public ServiceException serviceException() {
        return new ServiceException(this.resultCode, this.resultMsg);
    }
}
