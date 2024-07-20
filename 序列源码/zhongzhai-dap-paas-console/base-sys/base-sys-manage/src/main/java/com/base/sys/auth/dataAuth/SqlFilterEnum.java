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
    KAFKA_GET_CLUSTER_NO_TENANT("com.dap.paas.console.mq.kafka.entity.KafkaCluster.getClusterByIdNoTenant"),

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
    CONFIG_NODE_JOB_UPDATECONTAINERJOB("com.dap.paas.console.config.portal.entity.ConfigServiceNode.updateContainerJob"),
    CONFIG_NODE_JOB_DELETECONTAINERJOB("com.dap.paas.console.config.portal.entity.ConfigServiceNode.deleteContainerNodeJob"),
    CONFIG_NODE_JOB_QUERYLISTJOB("com.dap.paas.console.config.portal.entity.ConfigServiceNode.queryListJob"),
    CONFIG_NODE_JOB_SAVEJOB("com.dap.paas.console.config.portal.entity.ConfigServiceNode.saveObjectJob"),

    //**************序列中心定时集群节点查询*************
    SEQ_CLUSTER_JOB("com.dap.paas.console.seq.entity.SeqServiceCluster.queryListJob"),
    SEQ_CLUSTER_JOB_UPDATE("com.dap.paas.console.seq.entity.SeqServiceCluster.updateJob"),
    SEQ_NODE_JOB_UPDATE("com.dap.paas.console.seq.entity.SeqServiceNode.updateJob"),
    SEQ_NODE_JOB_QUERYNODES("com.dap.paas.console.seq.entity.SeqServiceNode.queryNodes"),
    SEQ_SDK_MONITOR_QUERY("com.dap.paas.console.seq.entity.SeqSdkMonitor.queryListNoPermission"),
    SEQ_SDK_MONITOR_UPDATE("com.dap.paas.console.seq.entity.SeqSdkMonitor.updateNoPermission"),
    SEQ_USE_CONDITION_DELETE("com.dap.paas.console.seq.entity.SeqUseCondition.deleteAttributeData"),

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
    BASIC_KUBERNETES_INFO_UPDATE("com.dap.paas.console.basic.entity.BaseSysKubernetesInfo.updateStatus"),
    BASIC_KUBERNETES_INFO_ID("com.dap.paas.console.basic.entity.BaseSysKubernetesInfo.getObjectById"),

    BASIC_SYNC_FIND_CITY("com.dap.paas.console.basic.entity.City.findExist"),
    BASIC_SYNC_BATCHSAVE_CITY("com.dap.paas.console.basic.entity.City.saveCityBatch"),
    BASIC_SYNC_FIND_MACHINEROOM("com.dap.paas.console.basic.entity.MachineRoom.findExist"),
    BASIC_SYNC_BATCHSAVE_MACHINEROOM("com.dap.paas.console.basic.entity.MachineRoom.saveMachineRoomBatch"),
    BASIC_SYNC_FIND_MACHINE("com.dap.paas.console.basic.entity.Machine.findExist"),
    BASIC_SYNC_BATCHSAVE_MACHINE("com.dap.paas.console.basic.entity.Machine.saveMachineBatch"),
    BASIC_SYNC_FIND_APPLICATION("com.dap.paas.console.basic.entity.Application.findExist"),
    BASIC_SYNC_BATCHSAVE_APPLICATION("com.dap.paas.console.basic.entity.Application.saveApplicationBatch"),
    BASIC_SAVE_USER("com.base.sys.api.entity.BaseSysUser.saveObject"),
    BASIC_USER_ACCOUONT("com.base.sys.api.entity.BaseSysUser.getByAccount"),
    BASIC_SAVE_TENANT("com.base.sys.api.entity.TenantManageEntity.saveObject"),
    /**
     * 路由sql不进行tenantId过滤
     */
    ROUTER_getRouteRuleConfigInfoListByUseStatus("com.dap.route.dao.RuleConfigInfoDTOMapper.getRouteRuleConfigInfoListByUseStatus"),
    ROUTER_saveRouteRuleConfigInfo("com.dap.route.dao.RuleConfigInfoDTOMapper.saveRouteRuleConfigInfo"),
    ROUTER_updateRouteRuleConfigInfo("com.dap.route.dao.RuleConfigInfoDTOMapper.updateRouteRuleConfigInfo"),
    ROUTER_updateStatusById("com.dap.route.dao.RuleConfigInfoDTOMapper.updateStatusById"),
    ROUTER_deleteRouteRuleConfigInfoList("com.dap.route.dao.RuleConfigInfoDTOMapper.deleteRouteRuleConfigInfoList"),
    ROUTER_deleteRouteRuleConfigInfoById("com.dap.route.dao.RuleConfigInfoDTOMapper.deleteRouteRuleConfigInfoById"),
    ROUTER_getRouteRuleConfigInfoById("com.dap.route.dao.RuleConfigInfoDTOMapper.getRouteRuleConfigInfoById"),
    ROUTER_getRouteRuleConfigInfoBySystemAndService("com.dap.route.dao.RuleConfigInfoDTOMapper.getRouteRuleConfigInfoBySystemAndService"),
    ROUTER_getRuleConfigIdBySystemAndService("com.dap.route.dao.RuleConfigInfoDTOMapper.getRuleConfigIdBySystemAndService"),
    ROUTER_getRouteRuleConfigInfoList("com.dap.route.dao.RuleConfigInfoDTOMapper.getRouteRuleConfigInfoList"),
    ROUTER_getRouteRuleConfigInfoTotal("com.dap.route.dao.RuleConfigInfoDTOMapper.getRouteRuleConfigInfoTotal"),
    ROUTER_deleteConfigInfoDTO("com.dap.route.dao.RuleConfigInfoDTOMapper.deleteConfigInfoDTO"),

    ROUTER_insert("com.dap.route.dao.RouteTableMapper.insert"),
    ROUTER_update("com.dap.route.dao.RouteTableMapper.update"),
    ROUTER_routeTables("com.dap.route.dao.RouteTableMapper.routeTables"),
    ROUTER_countRouteTables("com.dap.route.dao.RouteTableMapper.countRouteTables"),
    ROUTER_delete("com.dap.route.dao.RouteTableMapper.delete"),

    ROUTER_pageList("com.dap.route.dao.RouteAreaDTOMapper.pageList"),
    ROUTER_getAllList("com.dap.route.dao.RouteAreaDTOMapper.getAllList"),
    ROUTER_rootNodeList("com.dap.route.dao.RouteAreaDTOMapper.rootNodeList"),
    ROUTER_pageListTotal("com.dap.route.dao.RouteAreaDTOMapper.pageListTotal"),
    ROUTER_saveArea("com.dap.route.dao.RouteAreaDTOMapper.saveArea"),
    ROUTER_getRepeat("com.dap.route.dao.RouteAreaDTOMapper.getRepeat"),
    ROUTER_updateArea("com.dap.route.dao.RouteAreaDTOMapper.updateArea"),
    ROUTER_deleteAreaById("com.dap.route.dao.RouteAreaDTOMapper.deleteAreaById"),
    ROUTER_deleteAreaByCode("com.dap.route.dao.RouteAreaDTOMapper.deleteAreaByCode"),
    ROUTER_getAreaByCode("com.dap.route.dao.RouteAreaDTOMapper.getAreaByCode"),
    ROUTER_findById("com.dap.route.dao.RouteAreaDTOMapper.findById"),
    ROUTER_findList("com.dap.route.dao.RouteAreaDTOMapper.findList"),

    ROUTER_saveOriginalDTO("com.dap.route.dao.OriginalDTOMapper.saveOriginalDTO"),
    ROUTER_saveOriginalDTOList("com.dap.route.dao.OriginalDTOMapper.saveOriginalDTOList"),
    ROUTER_getOriginalDTOList("com.dap.route.dao.OriginalDTOMapper.getOriginalDTOList"),
    ROUTER_getOriginalDTOListByRuleIdList("com.dap.route.dao.OriginalDTOMapper.getOriginalDTOListByRuleIdList"),
    ROUTER_getOriginalDTOListByRuleId("com.dap.route.dao.OriginalDTOMapper.getOriginalDTOListByRuleId"),
    ROUTER_updateOriginalDTO("com.dap.route.dao.OriginalDTOMapper.updateOriginalDTO"),
    ROUTER_deleteOriginalDTOList("com.dap.route.dao.OriginalDTOMapper.deleteOriginalDTOList"),
    ROUTER_deleteOriginalDTOById("com.dap.route.dao.OriginalDTOMapper.deleteOriginalDTOById"),
    ROUTER_getOriginalDTOById("com.dap.route.dao.OriginalDTOMapper.getOriginalDTOById"),
    ROUTER_deleteOriginalDTOByRuleConfigId("com.dap.route.dao.OriginalDTOMapper.deleteOriginalDTOByRuleConfigId"),
    ROUTER_deleteOriginalDTOByRuleConfigIds("com.dap.route.dao.OriginalDTOMapper.deleteOriginalDTOByRuleConfigIds"),
    ROUTER_deleteOriginalDTO("com.dap.route.dao.OriginalDTOMapper.deleteOriginalDTO"),

    ROUTER_saveHisTargetDTO("com.dap.route.dao.HisTargetDTOMapper.saveHisTargetDTO"),
    ROUTER_saveHisTargetDTOList("com.dap.route.dao.HisTargetDTOMapper.saveHisTargetDTOList"),
    ROUTER_getHisTargetDTOList("com.dap.route.dao.HisTargetDTOMapper.getHisTargetDTOList"),
    ROUTER_getHisTargetDTOListByRuleId("com.dap.route.dao.HisTargetDTOMapper.getHisTargetDTOListByRuleId"),
    ROUTER_updateHisTargetDTO("com.dap.route.dao.HisTargetDTOMapper.updateHisTargetDTO"),
    ROUTER_deleteHisTargetDTOList("com.dap.route.dao.HisTargetDTOMapper.deleteHisTargetDTOList"),
    ROUTER_deleteHisTargetDTOById("com.dap.route.dao.HisTargetDTOMapper.deleteHisTargetDTOById"),
    ROUTER_getHisTargetDTOById("com.dap.route.dao.HisTargetDTOMapper.getHisTargetDTOById"),
    ROUTER_getVersionHisTargetDTOListByRuleId("com.dap.route.dao.HisTargetDTOMapper.getVersionHisTargetDTOListByRuleId"),

    ROUTER_saveHisRuleDTO("com.dap.route.dao.HisRuleDTOMapper.saveHisRuleDTO"),
    ROUTER_saveHisRuleDTOList("com.dap.route.dao.HisRuleDTOMapper.saveHisRuleDTOList"),
    ROUTER_getHisRuleDTOList("com.dap.route.dao.HisRuleDTOMapper.getHisRuleDTOList"),
    ROUTER_getHisRuleDTOListByRuleConfigId("com.dap.route.dao.HisRuleDTOMapper.getHisRuleDTOListByRuleConfigId"),
    ROUTER_updateHisRuleDTO("com.dap.route.dao.HisRuleDTOMapper.updateHisRuleDTO"),
    ROUTER_deleteHisRuleDTOList("com.dap.route.dao.HisRuleDTOMapper.deleteHisRuleDTOList"),
    ROUTER_deleteHisRuleDTOById("com.dap.route.dao.HisRuleDTOMapper.deleteHisRuleDTOById"),
    ROUTER_getHisRuleDTOById("com.dap.route.dao.HisRuleDTOMapper.getHisRuleDTOById"),
    ROUTER_getVersionHisRuleDTOListByRuleConfigId("com.dap.route.dao.HisRuleDTOMapper.getVersionHisRuleDTOListByRuleConfigId"),

    ROUTER_saveHisRouteRuleConfigInfo("com.dap.route.dao.HisRuleConfigInfoDTOMapper.saveHisRouteRuleConfigInfo"),
    ROUTER_getMaxVersion("com.dap.route.dao.HisRuleConfigInfoDTOMapper.getMaxVersion"),
    ROUTER_getRouteRuleConfigVersionList("com.dap.route.dao.HisRuleConfigInfoDTOMapper.getRouteRuleConfigVersionList"),
    ROUTER_getRouteRuleConfigVersionTotal("com.dap.route.dao.HisRuleConfigInfoDTOMapper.getRouteRuleConfigVersionTotal"),
    ROUTER_getRouteRuleConfigVersionById("com.dap.route.dao.HisRuleConfigInfoDTOMapper.getRouteRuleConfigVersionById"),
    ROUTER_getRouteRuleConfigInfoById2("com.dap.route.dao.HisRuleConfigInfoDTOMapper.getRouteRuleConfigInfoById"),
    ROUTER_getRouteRuleConfigVersion("com.dap.route.dao.HisRuleConfigInfoDTOMapper.getRouteRuleConfigVersion"),

    ROUTER_getMaxVersion2("com.dap.route.dao.HisRouteAreaDTOMapper.getMaxVersion"),
    ROUTER_batchInsert("com.dap.route.dao.HisRouteAreaDTOMapper.batchInsert"),
    ROUTER_getHisByCodeAndVersion("com.dap.route.dao.HisRouteAreaDTOMapper.getHisByCodeAndVersion"),
    ROUTER_getRouteAreaVersionList("com.dap.route.dao.HisRouteAreaDTOMapper.getRouteAreaVersionList"),
    ROUTER_getRouteAreaVersionTotal("com.dap.route.dao.HisRouteAreaDTOMapper.getRouteAreaVersionTotal"),

    ROUTER_getRuleDTO("com.dap.route.dao.RuleDTOMapper.getRuleDTO"),
    ROUTER_getRuleDTOListByRuleConfigId("com.dap.route.dao.RuleDTOMapper.getRuleDTOListByRuleConfigId"),
    ROUTER_getRuleDTOListByRuleConfigIdAndStatus("com.dap.route.dao.RuleDTOMapper.getRuleDTOListByRuleConfigIdAndStatus"),
    ROUTER_saveRuleDTO("com.dap.route.dao.RuleDTOMapper.saveRuleDTO"),
    ROUTER_saveRuleDTOList("com.dap.route.dao.RuleDTOMapper.saveRuleDTOList"),
    ROUTER_getRuleDTOList("com.dap.route.dao.RuleDTOMapper.getRuleDTOList"),
    ROUTER_getRuleDTOListByRuleConfigIdList("com.dap.route.dao.RuleDTOMapper.getRuleDTOListByRuleConfigIdList"),
    ROUTER_updateRuleDTO("com.dap.route.dao.RuleDTOMapper.updateRuleDTO"),
    ROUTER_deleteRuleDTOList("com.dap.route.dao.RuleDTOMapper.deleteRuleDTOList"),
    ROUTER_deleteRuleDTOById("com.dap.route.dao.RuleDTOMapper.deleteRuleDTOById"),
    ROUTER_getRuleDTOById("com.dap.route.dao.RuleDTOMapper.getRuleDTOById"),
    ROUTER_deleteRuleDTOByRuleConfigId("com.dap.route.dao.RuleDTOMapper.deleteRuleDTOByRuleConfigId"),
    ROUTER_deleteRuleDTOByRuleConfigIds("com.dap.route.dao.RuleDTOMapper.deleteRuleDTOByRuleConfigIds"),
    ROUTER_deleteRuleInfoDTO("com.dap.route.dao.RuleDTOMapper.deleteRuleInfoDTO"),

    ROUTER_saveTargetDTO("com.dap.route.dao.TargetDTOMapper.saveTargetDTO"),
    ROUTER_saveTargetDTOList("com.dap.route.dao.TargetDTOMapper.saveTargetDTOList"),
    ROUTER_getTargetDTOList("com.dap.route.dao.TargetDTOMapper.getTargetDTOList"),
    ROUTER_getTargetDTOListByRuleIdList("com.dap.route.dao.TargetDTOMapper.getTargetDTOListByRuleIdList"),
    ROUTER_getTargetDTOListByRuleId("com.dap.route.dao.TargetDTOMapper.getTargetDTOListByRuleId"),
    ROUTER_updateTargetDTO("com.dap.route.dao.TargetDTOMapper.updateTargetDTO"),
    ROUTER_deleteTargetDTOList("com.dap.route.dao.TargetDTOMapper.deleteTargetDTOList"),
    ROUTER_deleteTargetDTOById("com.dap.route.dao.TargetDTOMapper.deleteTargetDTOById"),
    ROUTER_getTargetDTOById("com.dap.route.dao.TargetDTOMapper.getTargetDTOById"),
    ROUTER_deleteTargetDTOByRuleConfigId("com.dap.route.dao.TargetDTOMapper.deleteTargetDTOByRuleConfigId"),
    ROUTER_deleteTargetDTOByRuleConfigIds("com.dap.route.dao.TargetDTOMapper.deleteTargetDTOByRuleConfigIds"),
    ROUTER_deleteRuleTargetDTO("com.dap.route.dao.TargetDTOMapper.deleteRuleTargetDTO"),

    ROUTER_saveHisOriginalDTO("com.dap.route.dao.HisOriginalDTOMapper.saveHisOriginalDTO"),
    ROUTER_saveHisOriginalDTOList("com.dap.route.dao.HisOriginalDTOMapper.saveHisOriginalDTOList"),
    ROUTER_getHisOriginalDTOList("com.dap.route.dao.HisOriginalDTOMapper.getHisOriginalDTOList"),
    ROUTER_getHisOriginalDTOListByRuleId("com.dap.route.dao.HisOriginalDTOMapper.getHisOriginalDTOListByRuleId"),
    ROUTER_updateHisOriginalDTO("com.dap.route.dao.HisOriginalDTOMapper.updateHisOriginalDTO"),
    ROUTER_deleteHisOriginalDTOList("com.dap.route.dao.HisOriginalDTOMapper.deleteHisOriginalDTOList"),
    ROUTER_deleteHisOriginalDTOById("com.dap.route.dao.HisOriginalDTOMapper.deleteHisOriginalDTOById"),
    ROUTER_getHisOriginalDTOById("com.dap.route.dao.HisOriginalDTOMapper.getHisOriginalDTOById"),
    ROUTER_getVersionHisOriginalDTOListByRuleId("com.dap.route.dao.HisOriginalDTOMapper.getVersionHisOriginalDTOListByRuleId"),


    /**
     * 审计日志自动任务查询和推送
     */
    BASELOGAUDIT_QUERYLISTASYNCNOTASYNCSTATUS("com.base.sys.api.entity.BaseLogAudit.queryListAsyncNotAsyncStatus"),
    BASELOGAUDIT_UPDATEOBJECT("com.base.sys.api.entity.BaseLogAudit.updateObject"),

    CLIENTOPERATE_QUERYLISTASYNCNOTASYNCSTATUS("com.dap.paas.console.basic.entity.ClientOperate.queryListAsyncNotAsyncStatus"),
    CLIENTOPERATE_UPDATEOBJECT("com.dap.paas.console.basic.entity.ClientOperate.updateObject"),

    CACHE_SYS_LOG_AUDIT_QUERYLISTASYNCNOTASYNCSTATUS("com.dap.paas.console.cache.entity.CacheLogAudit.queryListAsyncNotAsyncStatus"),
    ;


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
