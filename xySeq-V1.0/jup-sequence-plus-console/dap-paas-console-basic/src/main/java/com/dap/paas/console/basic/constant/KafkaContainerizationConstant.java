package com.dap.paas.console.basic.constant;

/**
 * @author captjack
 */
public class KafkaContainerizationConstant {

    /**
     * kafka容器部署时是否对接外部zk 0：否   1： 是
     */
    public static final String BUTT_OUT_SIDE_ZK = "1";

    public static final String CREATE_ZK_CLUSTER = "/gientech/apis/kafka.gientech.io/v1alpha1/namespaces/{namespace}/zookeepers";

    public static final String OPERATION_ZK_CLUSTER = "/gientech/apis/kafka.gientech.io/v1alpha1/namespaces/{namespace}/zookeepers/{name}";

    public static final String CREATE_KFK_BROKER_CLUSTER = "/gientech/apis/kafka.gientech.io/v1alpha1/namespaces/{namespace}/kafkas";

    public static final String OPERATION_KFK_BROKER_CLUSTER = "/gientech/apis/kafka.gientech.io/v1alpha1/namespaces/{namespace}/kafkas/{name}";

}
