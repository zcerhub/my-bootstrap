package com.dap.paas.console.basic.constant;

/**
 * @author captjack
 */
public class RocketMqContainerizationConstant {

    /**
     * 创建nameserver集群
     */
    public static final String CREATE_NAMESERVER_CLUSTER = "/gientech/apis/rocketmq.gientech.io/v1alpha1/namespaces/{namespace}/nameservers";

    /**
     * 查询、更新、伸缩容、删除 nameserver集群
     */
    public static final String OPERATION_NAMESERVER_CLUSTER = "/gientech/apis/rocketmq.gientech.io/v1alpha1/namespaces/{namespace}/nameservers/{name}";

    /**
     * 创建broker集群
     */
    public static final String CREATE_BROKER_CLUSTER = "/gientech/apis/rocketmq.gientech.io/v1alpha1/namespaces/{namespace}/brokers";

    /**
     * 查询、更新、伸缩容、删除 broker集群
     */
    public static final String OPERATION_BROKER_CLUSTER = "/gientech/apis/rocketmq.gientech.io/v1alpha1/namespaces/{namespace}/brokers/{name}";

}
