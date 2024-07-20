package com.base.sys.api.common;

public enum ResultEnum {

    SUCCESS(200, "请求成功"),
    FAILED(201, "请求失败"),
    ADD(202, "添加成功"),
    UPDATE(203, "修改成功"),
    DELETED(204, "删除成功"),
    EXIST(207,"该数据已存在"),
    CODE_EXIST(208,"单元编码已存在，请核对"),
    INSTANCE_RUN(208,"节点在运行，不能删除"),
    INSTANCE_EXIST(209,"集群下存在节点，不能删除"),
    DELETE_ERROR(210,"配置数据已启用，不能删除，请先下线，并发配置"),
    //服务治理
    ZOOKPEER_EXCEPTION(401,"ZK连接异常"),
    CREATED(205,"创建成功"),
    PARAM_NOT_MATH(404,"参数无方法"),
    INSERT_ERROR(405,"新增出错"),
    //注册中新部署
    INVALID_REQUEST(400,"数据请求参数错误"),
    INTERNAL_SERVER_ERROR(500,"服务器发生错误"),
    TEST_RE(402, "集群连接失败，请核对配置数据"),
    CONFIG_TABLE(209, "配置项key健重复，请核对配置数据"),
    REGISTER_NODE_IS_NULL(211, "集群实例为空，请添加实例再进行检测"),
    TEST_SQL(403, "数据库连接失败，请核对配置数据"),
    //配置中心
    NAMESPACE_EXIST(207,"容器集群的命名空间已存在"),
    //序列管控端
    SEQ_SNOW_INFO_EMPTY(2032, "序列雪花算法，请求参数为空!");

    private Integer code;
    private String msg;

    private ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
