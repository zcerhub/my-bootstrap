package com.dap.paas.console.basic.constant;

/**
 * @author captjack
 */
public class CacheContainerizationConstant {

    /**
     * 容器内镜像ssh默认用户密码
     */
    public static final int CONTAINER_SSHD_DEFAULT_PORT = 22;
    public static final String CONTAINER_SSHD_DEFAULT_USER = "root";
    public static final String CONTAINER_SSHD_DEFAULT_PASSWORD = "Gientech@123";

    /**
     * 创建缓存集群
     */
    public static final String CREATE_CACHE_CLUSTER = "/apis/cache.tongdun.net/v1alpha1/namespaces/{namespace}/redisclusters";

    /**
     * 查询、更新、伸缩容、删除 缓存集群
     */
    public static final String OPERATION_CACHE_CLUSTER = "/apis/cache.tongdun.net/v1alpha1/namespaces/{namespace}/redisclusters/{name}";

    /**
     * 查询缓存集群 pod
     */
    public static final String QUERY_CACHE_CLUSTER_POD = "/api/v1/namespaces/{namespace}/pods";

    /**
     * 查询缓存集群service信息
     */
    public static final String OPERATION_CACHE_CLUSTER_SERVICE = "/api/v1/namespaces/{namespace}/services";


    /**
     * 查询缓存集群指定service信息
     */
    public static final String QUERY_OPERATION_CACHE_CLUSTER_SERVICE_BY_SERVICESNAME = "/api/v1/namespaces/{namespace}/services/{name}";

    /**
     * 根据PVC名称删除PVC  名称说明：redis-data-{podname}
     */
    public static final String DELETE_PVC_POD = "/api/v1/namespaces/{namespace}/persistentvolumeclaims/{name}";


    /**
     * 查看pv
     */
    public static final String QUERY_ALL_PV_POD = "/api/v1/persistentvolumes";

    /**
     * 删除pv
     */
    public static final String DELETE_PV_POD = "/api/v1/persistentvolumes/{name}";

}
