package com.dap.paas.console.basic.constant;

/**
 * @author captjack
 */
public class ContainerizationConstant {

    /**
     * 部署模式:容器部署
     */
    public static final String DEPLOY_MODE_CONTAINER = "1";

    /**
     * 集群状态:待部署
     */
    public static final String CLUSTER_STATE_UNDEPLOYED = "0";

    /**
     * 集群状态:已部署
     */
    public static final String CLUSTER_STATE_DEPLOYED = "1";

    /**
     * 部署模式:虚机部署
     */
    public static final String DEPLOY_MODE_VIRTUAL_MACHINE = "2";

    public static final String CHECK_AGENT_STATE = "/healthz";

    public static final String AGENT_STATE_HEALTH = "1";

    public static final String AGENT_STATE_UNHEALTH = "0";

    /**
     * 查询集群 pod
     */
    public static final String QUERY_CLUSTER_POD = "/gientech/api/v1/namespaces/{namespace}/pods";

    public static final String MEDIA_TYPE_PATCH = "application/merge-patch+json";

    public static final String HTTP_REQUEST = "http://";

    public static final String HTTPS_REQUEST = "https://";

    /**
     * 查询rocketmq、kafka、zk容器命名空间 namespaces 请求地址
     */
    public static final String QUERY_CONTAINER_NAMESPACES_URL = "/gientech/api/v1/namespaces";

    /**
     * 查询REDIS容器命名空间 namespaces 请求地址
     */
    public static final String QUERY_REDIS_CONTAINER_NAMESPACES_URL = "/api/v1/namespaces";

    /**
     * 查询容器 StorageClass
     */
    public static final String QUERY_CONTAINER_STORAGE_CLASSES = "/gientech/apis/storage.k8s.io/v1/storageclasses";

    /**
     * 查询redis代理容器 StorageClass
     */
    public static final String QUERY_REDIS_CONTAINER_STORAGE_CLASSES = "/apis/storage.k8s.io/v1/storageclasses";

    /**
     * 查询镜像拉取策略
     */
    public static final String QUERY_IMAGE_PULL_SECRETS = "/gientech/api/v1/namespaces/{namespace}/secrets";

    /**
     * 查询容器事件日志
     */
    public static final String QUERY_CONTAINER_EVENT_LOG = "/gientech/api/v1/namespaces/{namespace}/events?fieldSelector=involvedObject.kind=Pod,involvedObject.name={pod_name}";

    /**
     * 查询pod启动日志
     */
    public static final String QUERY_POD_START_LOG = "/gientech/api/v1/namespaces/{namespace}/pods/{pod_name}/log";

    /**
     * 查询nameserver端口
     */
    public static final String QUERY_CLUSTER_NAMESERVER_PORT = "/gientech/api/v1/namespaces/{namespace}/services/{name}-nameserver";

    /**
     * 根据PVC名称删除PVC  名称说明：
     * redis  redis-data-{podname}
     * 消息    {clusterName}-{组件类型}-{podname}   组件类型 nameserver、broker、zk
     */
    public static final String DELETE_PVC_POD = "/gientech/api/v1/namespaces/{namespace}/persistentvolumeclaims/{name}";


    /**
     * 查看pv
     */
    public static final String QUERY_ALL_PV_POD = "/gientech/api/v1/persistentvolumes";

    /**
     * 删除pv
     */
    public static final String DELETE_PV_POD = "/gientech/api/v1/persistentvolumes/{name}";
}
