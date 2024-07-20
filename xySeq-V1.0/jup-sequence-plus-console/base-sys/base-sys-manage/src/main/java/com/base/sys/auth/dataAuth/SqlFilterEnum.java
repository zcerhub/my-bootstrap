package com.base.sys.auth.dataAuth;

import java.util.ArrayList;
import java.util.List;


public enum SqlFilterEnum {

    GET_TENANT_ON_LOGIN("com.base.sys.api.entity.TenantManageEntity.getIdByCodeOnLogin"),
    CACHE_CLUSTER_LIST_DATA("com.dap.paas.console.cache.entity.Cluster.queryClusterPage"),
    CACHE_BASESYSUSERLIST_DATA("com.base.sys.api.entity.BaseSysUser.getObjectByMap"),
    CACHE_CLUSTER_NODE_STATUS_MONITOR("com.dap.paas.console.cache.entity.Cluster.nodeStatusMonitorGetCluster"),

    // -----------------mq-kafka-start------------------------
    KAFKA_ZOOKEEPER_SAVE("com.dap.paas.console.mq.kafka.entity.KafkaZookeeper.saveZookeeper"),
    KAFKA_ZOOKEEPER_UPDATE("com.dap.paas.console.mq.kafka.entity.KafkaZookeeper.updateWithoutInterceptor"),
    KAFKA_ZOOKEEPER_QUERY_LIST("com.dap.paas.console.mq.kafka.entity.KafkaZookeeper.queryListWithoutInterceptor"),
    KAFKA_INSTANCE_QUERY_LIST("com.dap.paas.console.mq.kafka.entity.KafkaClusterInstance.queryListWithoutInterceptor"),
    KAFKA_INSTANCE_UPDATE("com.dap.paas.console.mq.kafka.entity.KafkaClusterInstance.updateWithoutInterceptor"),
    KAFKA_QUERY_LIST("com.dap.paas.console.mq.kafka.entity.KafkaCluster.queryListWithoutInterceptor"),
    KAFKA_CLUSTER_QUERY_ID("com.dap.paas.console.mq.kafka.entity.KafkaCluster.getClusterById"),
    KAFKA_GET_TOPIC_IDS("com.dap.paas.console.mq.kafka.entity.KafkaTopic.getIdsByClusterId"),
    KAFKA_SAVE_TOPICS("com.dap.paas.console.mq.kafka.entity.KafkaTopic.saveTopics"),
    KAFKA_DELETE_TOPICS("com.dap.paas.console.mq.kafka.entity.KafkaTopic.deleteByClusterId"),
    KAFKA_SAVE_PARTITIONS("com.dap.paas.console.mq.kafka.entity.KafkaPartition.savePartitions"),
    KAFKA_DELETE_PARTITIONS("com.dap.paas.console.mq.kafka.entity.KafkaPartition.deleteByTopicIds"),
    KAFKA_DELETE_GROUP("com.dap.paas.console.mq.kafka.entity.KafkaGroup.deleteByClusterId"),
    KAFKA_SAVE_GROUP("com.dap.paas.console.mq.kafka.entity.KafkaGroup.saveGroups"),
    KAFKA_SAVE_RATE_DETAILS("com.dap.paas.console.mq.kafka.entity.KafkaRateDetail.saveRateDetails"),
    KAFKA_TRUNCATE_RATE_DETAILS("com.dap.paas.console.mq.kafka.entity.KafkaRateDetail.truncateDetails"),
    KAFKA_DELETE_CONSUMER_DETAILS("com.dap.paas.console.mq.kafka.entity.KafkaConsumerDetail.deleteByClusterId"),
    KAFKA_SAVE_CONSUMER_DETAILS("com.dap.paas.console.mq.kafka.entity.KafkaConsumerDetail.saveConsumers"),
    KAFKA_QUERY_CONSUMER_DETAILS("com.dap.paas.console.mq.kafka.entity.KafkaConsumerDetail.queryList"),
    KAFKA_SAVE_SIZE_DETAILS("com.dap.paas.console.mq.kafka.entity.KafkaSizeDetail.saveSizeDetails"),
    KAFKA_TRUNCATE_SIZE_DETAILS("com.dap.paas.console.mq.kafka.entity.KafkaSizeDetail.truncateDetails"),
    KAFKA_SAVE_SIZE_HISTORIES("com.dap.paas.console.mq.kafka.entity.KafkaSizeHistory.saveSizeHistories"),
    KAFKA_QUERY_SIZE_HISTORIES("com.dap.paas.console.mq.kafka.entity.KafkaSizeHistory.queryListByDate"),
    KAFKA_DELETE_SIZE_HISTORIES("com.dap.paas.console.mq.kafka.entity.KafkaSizeHistory.deleteByDate"),
    // -----------------mq-kafka-end------------------------

    // -----------------config-start------------------------
    GET_ALL_CONFIG_REALESE("com.dap.paas.console.config.portal.entity.ConfigClientTrace.selectAllConfigRelease"),
    CLEAR_CONFIG_TRACE("com.dap.paas.console.config.portal.entity.ConfigClientTrace.clearConfigClientTrace"),
    //-----------------config-end------------------------
    GET_TENANT_LIST("com.base.sys.api.entity.TenantManageEntity.queryList"),
    GET_TENANT_BY_ID("com.base.sys.api.entity.TenantManageEntity.getObjectById"),
    // -----------------config-openApi------------------------
//    POST_RELEASE_PUSH_SAVE("com.dap.paas.console.config.portal.entity.ConfigRelease.saveObject"),
//    POST_RELEASE_PUSH_UPDATE("com.dap.paas.console.config.portal.entity.ConfigRelease.updateObject"),
//    POST_RELEASE_QUERYLIST("com.dap.paas.console.config.portal.entity.ConfigRelease.queryList"),
//    POST_HISTORY_PUSH_SAVE("com.dap.paas.console.config.portal.entity.ConfigHistory.saveObject"),
//    POST_HISTORY_USH_UPDATE("com.dap.paas.console.config.portal.entity.ConfigHistory.updateObject"),
    POST_HISTORY_QUERYLIST("com.dap.paas.console.config.portal.entity.ConfigHistory.queryList"),
    QUERYCLUSTERLISTBYENVCODE("com.dap.paas.console.config.portal.entity.ConfigEvnConfiguration.queryClusterListByEnvCode"),
    CONFIGSERVICECLUSTER_GETOBJECTBYID("com.dap.paas.console.config.portal.entity.ConfigServiceCluster.getObjectById"),
//    POST_AUDIT_QUERYLIST("com.dap.paas.console.config.portal.entity.ConfigReleaseAuditInfo.saveObject"),
    //-----------------config-end------------------------
    // ---------------------缓存特殊处理 start-------------------------------
    /////////监控处理
    GET_CACHE_CLUSTER_ALL("com.dap.paas.console.cache.entity.Cluster.getClusterAll"),
    UPDATE_CLUSTER_OF_NODE_STATUS_MONITOR("com.dap.paas.console.cache.entity.Cluster.updateClusterOfNodeStatusMonitorJob"),
    GET_CACHE_CLUSTER_NODE("com.dap.paas.console.cache.entity.ClusterNode.getClusterNode"),
    SAVE_CACHE_MONITOR("com.dap.paas.console.cache.entity.NodeMonitor.saveNodeMonitor"),
    UPDATE_NODE_STATUS("com.dap.paas.console.cache.entity.ClusterNode.updateNodeStatus"),
    CLUSTER_NODE_GETOBJECTBYID("com.dap.paas.console.cache.entity.ClusterNode.getObjectById"),
    SAVE_CACHE_MONITOR_BATCH("com.dap.paas.console.cache.entity.NodeMonitor.insertBatchNodeMonitor"),
    GET_REDIS_CLUSTER_INFO("com.dap.paas.console.cache.entity.NodeMonitor.getRedisClusterInfo"),
    CACHE_GET_OBJECT_BY_MAP("com.dap.paas.console.cache.entity.CacheClientMonitor.getObjectByMap"),
    CACHE_SAVE_OBJECT_BY_MAP("com.dap.paas.console.cache.entity.CacheClientMonitor.saveObject"),
    CACHE_UPDATE_OBJECT_BY_MAP("com.dap.paas.console.cache.entity.CacheClientMonitor.updateObject"),
    CACHE_GET_OBJECT_BY_ID("com.dap.paas.console.cache.entity.Cluster.getObjectById"),
    CLUSTER_NODE_QUERYLIST("com.dap.paas.console.cache.entity.ClusterNode.queryList"),

    //缓存用户列表查询
    CACHE_USER_QUERY_PAGE("com.dap.paas.console.cache.entity.CacheUsers.queryPage"),

    // ---------------------缓存特殊处理 end-------------------------------

    // -----------------mq-rocketmq-start------------------------

    ROCKET_INSTANCE_MONITOR_TOTAL("com.dap.paas.console.mq.rocketmq.entity.Instance.selectTotal"),
    ROCKET_INSTANCE_QLIST("com.dap.paas.console.mq.rocketmq.entity.Instance.queryList"),

    ROCKET_INSTANCE_NAMESRC("com.dap.paas.console.mq.rocketmq.entity.NamesrvConf.getObjectById"),
    ROCKET_INSTANCE_MACHINE("com.dap.paas.console.basic.entity.Machine.getObjectById"),
    ROCKET_QUERY_LIST("com.dap.paas.console.mq.rocketmq.entity.Cluster.getQueryAllList"),
    ROCKET_QUERY_GETBYID("com.dap.paas.console.mq.rocketmq.entity.Cluster.getObjectById"),
    ROCKET_INSTANCE_LIST("com.dap.paas.console.mq.rocketmq.entity.Instance.queryNameSrvByCluster"),
    ROCKET_MONITOR_SAVE("com.dap.paas.console.mq.rocketmq.entity.RocketmqMonitoring.saveObject"),
    ROCKET_MONITOR_UPDATE("com.dap.paas.console.mq.rocketmq.entity.RocketmqMonitoring.updateObject"),
    ROCKET_MONITOR_BY_CLUSTERID("com.dap.paas.console.mq.rocketmq.entity.RocketmqMonitoring.getObjectByClusterId"),
    ROCKET_MONITOR_DEL("com.dap.paas.console.mq.rocketmq.entity.RocketmqMonitoring.delObjectAll"),
    ROCKET_MONITOR_QLIST("com.dap.paas.console.mq.rocketmq.entity.RocketmqMonitoring.queryList"),
    ROCKET_ROCKETBROKER_SAVE("com.dap.paas.console.mq.rocketmq.entity.RocketBrokerTrend.saveObject"),
    ROCKET_TOPICTOP_SAVE("com.dap.paas.console.mq.rocketmq.entity.RocketTopicTop.saveObject"),
    ROCKET_TOPICTOP_UPDATE("com.dap.paas.console.mq.rocketmq.entity.RocketTopicTop.updateObject"),
    ROCKET_TOPICTOP_TOTAL("com.dap.paas.console.mq.rocketmq.entity.RocketTopicTop.selectTotal"),
    ROCKET_TOPICTREND_SAVE("com.dap.paas.console.mq.rocketmq.entity.RocketTopicTrend.saveObject"),
    ROCKET_BROKERTOP_SAVE("com.dap.paas.console.mq.rocketmq.entity.RocketBrokerTop.saveObject"),
    ROCKET_BROKERTOP_TOTAL("com.dap.paas.console.mq.rocketmq.entity.RocketBrokerTop.selectTotal"),
    ROCKET_BROKERTOP_UPDATE("com.dap.paas.console.mq.rocketmq.entity.RocketBrokerTop.updateObject"),
    ROCKET_TOPICTREND_DEL("com.dap.paas.console.mq.rocketmq.entity.RocketTopicTrend.deleteByDate"),
    ROCKET_ROCKETBROKER_DEL("com.dap.paas.console.mq.rocketmq.entity.RocketBrokerTrend.deleteByDate"),
    ROCKET_PRODUCER_SELECT("com.dap.paas.console.mq.rocketmq.entity.ProducerConnection.queryList"),
    ROCKET_PRODUCER_DEL("com.dap.paas.console.mq.rocketmq.entity.ProducerConnection.deleteByClusterTopic"),
    ROCKET_PRODUCER_SAVE("com.dap.paas.console.mq.rocketmq.entity.ProducerConnection.saveObjects"),
    ROCKET_PRODUCER_DELBYDATE("com.dap.paas.console.mq.rocketmq.entity.ProducerConnection.deleteByDate"),
    API_FOR_CONNECT_GET_UNIT_ROUTE_LIST("com.dap.paas.console.mq.rocketmq.entity.ApiForConnect.queryRouteList"),
    API_FOR_CONNECT_GET_UNIT_CONFIG_LIST("com.dap.paas.console.mq.rocketmq.entity.ApiForConnect.queryConfigList"),
    API_FOR_CONNECT_GET_ROCKET_CLUSTER("com.dap.paas.console.mq.rocketmq.entity.ApiForConnect.getClusterById"),
    API_FOR_CONNECT_GET_UNIT("com.dap.paas.console.mq.rocketmq.entity.ApiForConnect.getUnitById"),
    API_FOR_CONNECT_GET_MACHINE("com.dap.paas.console.mq.rocketmq.entity.ApiForConnect.getMachineById"),
    API_FOR_CONNECT_GET_INSTANCE_LIST("com.dap.paas.console.mq.rocketmq.entity.ApiForConnect.queryInstanceList"),
    API_FOR_CONNECT_GET_UNITCODE_LIST("com.dap.paas.console.mq.rocketmq.entity.ApiForConnect.queryStructureUnitCode"),
    ROCKET_MESSAGEVIEW_SAVE("com.dap.paas.console.mq.rocketmq.entity.MessageView.saveObjects"),
    ROCKET_MESSAGEVIEW_QUERYLIST("com.dap.paas.console.mq.rocketmq.entity.MessageView.queryList"),
    ROCKET_MESSAGEVIEW_DELETE("com.dap.paas.console.mq.rocketmq.entity.MessageView.deleteByDate"),
    ROCKET_RESEND_QUERYLIST("com.dap.paas.console.mq.rocketmq.entity.ResendMessage.queryList"),
    ROCKET_RESEND_DELETE("com.dap.paas.console.mq.rocketmq.entity.ResendMessage.deleteByDate"),
    //声明一个构造方法

//  注册中心
    REGISTER_CLUSTER_QUERYLIST_JOB("com.dap.paas.console.register.entity.RegisterCluster.queryListJob"),
    REGISTER_INSTANCE_QUERYLIST_JOB("com.dap.paas.console.register.entity.RegisterInstance.queryInstances"),
    REGISTER_ICLUSTER_UPDATE_JOB("com.dap.paas.console.register.entity.RegisterCluster.updateJob"),
    REGISTER_INSTANCE_UPDATE_JOB("com.dap.paas.console.register.entity.RegisterInstance.updateJob"),

    //服务治理特殊处理
    OVERVIEW_JOB_QUERY("com.dap.paas.console.gov.server.po.GovAppConfigPo.OverviewJobQuery"),
    SAVE_OVERVIEW_JOB("com.dap.paas.console.gov.server.po.GovOverviewServerPo.saveOverviewJob"),
    JOB_SELECT_ALL("com.dap.paas.console.gov.server.po.GovOverviewServerPo.jobSelectAll"),
    DEL_JOB("com.dap.paas.console.gov.server.po.GovOverviewServerPo.delJob"),
    DEL_ALL_JOB("com.dap.paas.console.gov.server.po.GovOverviewServerPo.delAllJob"),
    SELECT_INSTANCE_NUM("com.dap.paas.console.gov.server.po.GovOverviewServerPo.selectInstanceNum"),
    JOB_GET_TENANT_CODE("com.dap.paas.console.gov.server.po.GovOverviewServerPo.jobGetTenantCode"),
    JOB_GET_APP_CODE("com.dap.paas.console.gov.server.po.GovOverviewServerPo.jobGetAppCode"),
    JOB_GET_PUBLISH_CLUSTER("com.dap.paas.console.gov.server.po.GovOverviewServerPo.jobGetPublishCluster"),
    JOB_GET_GOV_CLUSTER_BASE("com.dap.paas.console.gov.server.po.GovClusterBasePo.jobGetGovClusterBase"),
    GET_RES_TOTAL("com.dap.paas.console.gov.server.po.GovAppConfigPo.getResTotal"),
    JOB_GET_LIST_BASEID("com.dap.paas.console.gov.server.po.GovClusterConfigPo.queryListByBaseId"),

    //**************配置中心集群节点查询*************
    CONFIG_SERVER_INSTANCE("com.dap.paas.console.config.portal.dao.ConfigServerInstanceDAO.selectAll"),
    CONFIG_CLUSTER_JOB("com.dap.paas.console.config.portal.entity.ConfigCoordinatorEntity.getCoordinatorEntityById"),
    CONFIG_MULTICENTER_JOB("com.dap.paas.console.config.portal.entity.MulticenterClusterEntity.getMulticenterClusterById"),

    CONFIG_CLUSTER_JOB_QUERY("com.dap.paas.console.config.portal.entity.ConfigServiceCluster.queryListJob"),
    CONFIG_CLUSTER_JOB_UPDATE("com.dap.paas.console.config.portal.entity.ConfigServiceCluster.updateJob"),
    CONFIG_NODE_JOB_UPDATE("com.dap.paas.console.config.portal.entity.ConfigServiceNode.updateJob"),

    //**************序列中心定时集群节点查询*************
    SEQ_CLUSTER_JOB("com.dap.paas.console.seq.entity.SeqServiceCluster.queryListJob"),
    SEQ_CLUSTER_JOB_UPDATE("com.dap.paas.console.seq.entity.SeqServiceCluster.updateJob"),
    SEQ_NODE_JOB_UPDATE("com.dap.paas.console.seq.entity.SeqServiceNode.updateJob"),
    SEQ_NODE_JOB_QUERYNODES("com.dap.paas.console.seq.entity.SeqServiceNode.queryNodes"),
    SEQ_SDK_MONITOR_QUERY("com.dap.paas.console.seq.entity.SeqSdkMonitor.queryListNoPermission"),
    SEQ_SDK_MONITOR_UPDATE("com.dap.paas.console.seq.entity.SeqSdkMonitor.updateNoPermission"),
    SEQ_USE_CONDITION_DELETE("com.dap.paas.console.seq.entity.SeqUseCondition.deleteAttributeData"),
    SEQ_GET_LOCAL_OBJECT_BY_CODE("com.dap.paas.console.seq.entity.SeqDesignPo.getLocalObjectByCode"),
    SELECT_BY_DESIGN_ID("com.dap.paas.console.seq.entity.SeqInstanceRule.selectByDesignId"),
    SEQ_GET_LOCAL_OBJECT("com.dap.paas.console.seq.entity.SeqDesignPo.getLocalObject"),

    //**************基础信息同步数据*************
    BASIC_SYNC_GET_CITY("com.dap.paas.console.basic.entity.BasicSync.getCityByName"),
    BASIC_SYNC_SAVE_CITY("com.dap.paas.console.basic.entity.BasicSync.saveCity"),
    BASIC_SYNC_UPDATE_CITY("com.dap.paas.console.basic.entity.BasicSync.updateCity"),
    BASIC_SYNC_CLEAR_CITY("com.dap.paas.console.basic.entity.BasicSync.clearCity"),
    BASIC_SYNC_SAVE_ORGANIZATION("com.dap.paas.console.basic.entity.BasicSync.saveOrganization"),
    BASIC_SYNC_CLEAR_ORGANIZATION("com.dap.paas.console.basic.entity.BasicSync.clearOrganization"),
    BASIC_SYNC_GET_MACHINEROOM("com.dap.paas.console.basic.entity.BasicSync.getMachineRoomById"),
    BASIC_SYNC_UPDATE_MACHINEROOM("com.dap.paas.console.basic.entity.BasicSync.updateMachineRoom"),
    BASIC_SYNC_SAVE_MACHINEROOM("com.dap.paas.console.basic.entity.BasicSync.saveMachineRoom"),
    BASIC_SYNC_CLEAR_MACHINEROOM("com.dap.paas.console.basic.entity.BasicSync.clearMachineRoom"),
    BASIC_SYNC_DEL_MACHINEROOM("com.dap.paas.console.basic.entity.BasicSync.delMachineRoomById"),
    BASIC_SYNC_GET_MACHINE("com.dap.paas.console.basic.entity.BasicSync.getMachineById"),
    BASIC_SYNC_SAVE_MACHINE("com.dap.paas.console.basic.entity.BasicSync.saveMachine"),
    BASIC_SYNC_UPDATE_MACHINE("com.dap.paas.console.basic.entity.BasicSync.updateMachine"),
    BASIC_SYNC_CLEAR_MACHINE("com.dap.paas.console.basic.entity.BasicSync.clearMachine"),
    BASIC_SYNC_DEL_MACHINE("com.dap.paas.console.basic.entity.BasicSync.delMachineById"),
    BASIC_SYNC_GET_UNITIZATION("com.dap.paas.console.basic.entity.BasicSync.getUnitizationById"),
    BASIC_SYNC_SAVE_UNITIZATION("com.dap.paas.console.basic.entity.BasicSync.saveUnitization"),
    BASIC_SYNC_UPDATE_UNITIZATION("com.dap.paas.console.basic.entity.BasicSync.updateUnitization"),
    BASIC_SYNC_CLEAR_UNITIZATION("com.dap.paas.console.basic.entity.BasicSync.clearUnitization"),
    BASIC_SYNC_DEL_UNITIZATION("com.dap.paas.console.basic.entity.BasicSync.delUnitizationById"),
    BASIC_CONTAINER_INFO_QUERY_ALL("com.dap.paas.console.basic.entity.BaseSysKubernetesInfo.queryListByMap"),
    BASIC_CONTAINER_INFO_QUERY_ONE("com.dap.paas.console.basic.entity.BaseSysKubernetesInfo.getObjectByMap"),
    BASIC_CONTAINER_INFO_UPDATE("com.dap.paas.console.basic.entity.BaseSysKubernetesInfo.updateStatus");

    //***********************
    SqlFilterEnum(String sql){
        this.sql=sql;
    }
    public String getSql(){
        return this.sql;
    }

    private String sql;


    public static List toList() {
        List<String> list = new ArrayList();
        for (SqlFilterEnum sqlFilterEnum : SqlFilterEnum.values()) {
            list.add(sqlFilterEnum.getSql());
        }
        return list;
    }
}
