package com.dap.sequence.api.exception;

/**
 * @className ExceptionEnum
 * @description 异常枚举类
 * @author renle
 * @date 2023/11/24 17:43:54 
 * @version: V23.06
 */
public enum ExceptionEnum implements IBaseError {

    SUCCESS("S2001", "请求成功!"),

    SEQ_RECOVERY_NOT_FOUND("S2002", "没有配置序列编码为[{}]的序列！、无法回收对应序号!"),

    SEQ_ENGINE_CACHE_KEY_NOT_FOUND("S2003", "序列编码为[{}]的参数引擎缓存key无法找到!"),

    SEQ_NOT_FOUND("S2004", "需要在序列管控端配置对应序列[{}]!"),

    SEQ_FROM_SERVER_EMPTY("S2005", "从服务端获取序列为空!,序列编码为[{}]"),

    PLACE_DIGITS_LESS_THAN_FIXED("S2006", "参数位数小于替换符规则固定长度位数!,参数为[{}],固定长度为[{}]"),

    GET_SEQ_QUEUE_ERROR("S2007", "从缓存队列中获取序列失败!"),

    CHECK_WEIGHT_VALUE_ERROR("S2008", "需要校验的序列长度过长，生成校验位方法不能满足，当前校验的序列是[{}],能校验的最大长度是[{}]!"),

    SEQ_OPTIONAL_PARAM_VALID("S2009", "自选序列参数为空!,对应序列编码为[{}]"),

    SEQ_PARAM_RULE_EMPTY("S2010", "当前序列不包含自选规则!,对应序列编码为[{}]"),

    SEQ_OPTIONAL_SERIAL_SELECTED("S2011", "当前序号[{}]已被使用!"),

    SEQ_OPTIONAL_SELECTED_NOT_FOUND("S2012", "当前序号[{}]无法找到或者状态异常无法选中!"),

    SEQUENCE_EXIST("S2013", "序列[{}]已配置!"),

    SEQUENCE_NOT_EXIST("S2014", "序列不存在，序列编码为[{}]，序列名称为[{}]"),

    RULE_NOT_EMPTY("S2015", "规则不能为空，序列编码为[{}]"),

    RULE_EXIST("S2016", "规则模板已存在，模板名称为[{}]"),


    STATUS_INCONSISTENT("S2017","状态不一致，请重新选择"),

    METHOD_ARGUMENT_NOT_VALID_EXCEPTION("S2018","方法校验异常"),

    /**
     ＊序列开启自选能力的数字规则只能有一个
     */
    SEQ_OPTIONAL_RULE_MAX_1("S2019","序列开启自选能力的数字规则只能有一个"),

    SEQ_OPTIONAL_PLACE_RULE_MAX1("S2020","序列开启自选能力的数字规则和占位符规则只能有一个"),

    SEQ_NUM_MAXIMIZE("S2021","序列数字规则达到最大值，序列编码为[{}]"),

    SEQ_NOT_INCREASE_REQUEST_ERROR("S2022","非严格递增序列请通过普通接口获取，序列编码为[{}]"),

    SEQ_OPTIONAL_SEQ_VAL_ILLEGAL("S2023", "序列编号自选参数非法,必须为纯数字,序列编码为［｛｝］,自选参数为[{}]"),

    SEQ_RECOVERY_SERIAL_NOT_FOUND("S2024", "回收序列[{}]所回收的序号[{}]无法找到"),

    SEQ_OPTIONAL_VAL_NOT_MATCH_ERROR("S2025", "获取到的自选值与规则定义不符,获取的自选值为[{}],数字规则id为[{}]"),

    SEQ_OPTIONAL_NOT_MAX_START_ERROR("S2026","校验自选序号不能配置达到最大值从头开始,序列编码为[{}]"),

    LOAD_RULE_AND_SEQ_ENGINE_ERROR("S2027","加载序列配置对应的规则和更新SeqHolder对象失败"),

    RECOVERY_SEQ_EMPTY("S2028","回收序列[{}]要回收的序号[{}]为空"),


    // -------------------------平安内部没有的部分
    SEQ_RULE_EMPTY("S2029", "没有定义序列规则!"),
    SEQ_CLUSTER_NOT_FOUND("S2030", "集群服务不存在!"),
    SEQ_NODE_NOT_FOUND("S2031", "没有启动的序列节点!"),
    SEQ_SDK_MONITOR_INFO_EMPTY("S2032", "序列SDK监控信息参数为空!"),
    SEQ_UPDATE_HOLDER_FAILED("S2033", "动态修改Holder缓存失败!"),
    SEQ_ACCESS_KEY_CHECK_FAILED("S2034", "accessKey校验失败!"),
    SEQ_DATA_SOURCE_PARAM_EMPTY("S2035", "数据库参数为空!"),
    SEQ_UPDATE_DB_FILE_EMPTY("S2036", "操作数据库文件为空!"),
    SEQ_SWITCH_DB_FAILED("S2037", "数据库切换失败!"),
    GET_SEQ_OPTIONAL_DB_ERROR("S2038", "自选序列操作数据库失败!"),
    LOCK_KEY_EMPTY("S2039", "加锁key不能为空!"),
    LOCK_NOT_ADD_KEY("S2040", "尚未加锁!"),
    LOCK_NOT_HAVE_THREAD("S2041", "当前线程尚未持有锁!"),
    SEQ_OPTIONAL_RULE_VALID("S2042", "序列规则包含自选规则!"),
    SEQ_PLACE_RULE_VALID("S2043", "序列规则包含占位符规则!"),
    SEQ_OPTIONAL_SEQ_VALUE_VALID("S2044", "自选序列参数长度大于或者等于自选规则长度!"),
    SEQ_OPTIONAL_RULE_EMPTY("S2045", "当前序列不包含自选功能!,对应序列编码为[{}]"),
    SEQ_OPTIONAL_SERIAL_EMPTY("S2046", "当前序列不包含自选规则!,对应序列编码为[{}]"),
    SEQ_OPTIONAL_SERIAL_CANCEL("S2047", "序号选中时无法回收!"),
    SEQ_SERVER_CONNECT_FAIL("S2048", "序列服务端不可用!"),
    SEQ_GET_WORK_ID_FAIL("S2049", "当前应用未配置workId,且不能从序列服务端获取到workId,雪花算法无法使用！！！!"),
    SEQ_SNOW_INFO_EMPTY("S2050", "序列SDK，请求参数为空!"),
    SEQ_SNOW_PARAM_ERROR("S2051", "序列SDK，请求参数缺失!"),
    SEQ_SNOW_ADD_FAILURE("S2052", "序列服务端已无可用workId,获取失败!"),
    SEQ_RULE_NOT_EXIST("S2053", "序列规则无法找到!"),
    SEQ_OPTIONAL_PLACE_RULE_MAX_1("S2054", "序列开启自选能力的数字规则和占位符规则只能有一个!"),
    SEQ_INCREASE_REQUEST_ERROR("S2055", "严格递增序列[{}]请通过严格递增接口获取!"),
    SEQ_ASYNC_QUERY_FROM_SERVER_ERROR("S2056", "两段缓存均未从服务端获取到可用序列!"),
    SEQ_OPTIONAL_REPEAT("S2057", "自选序号和普通序号重复!"),
    GET_SEQ_OPTIONAL_SERIAL_NUM_ERROR("S2058", "自选序列号为空!"),
    CLOSE_STREAM_ERROR("S2059", "流关闭报错!"),
    CLIENT_MAKEUP_WAREHOUSE_ERROR("S2060", "客户端异步补仓异常!"),



    BODY_NOT_MATCH("S4400", "请求的数据格式不符!"),
    SIGNATURE_NOT_MATCH("S4401", "请求的数字签名不匹配!"),
    OPERATE_DB_ERROR("S4402", "操作数据库异常!"),
    OPERATE_SERVICE_ERROR("S4403", "服务操作异常!"),
    NOT_FOUND("S4404", "未找到该资源!"),

    SAVE_FAIL("S4405", "保存失败!"),
    INTERNAL_SERVER_ERROR("S4500", "服务器内部错误!"),
    SERVER_BUSY("S4503", "服务器正忙，请稍后再试!");

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
}
