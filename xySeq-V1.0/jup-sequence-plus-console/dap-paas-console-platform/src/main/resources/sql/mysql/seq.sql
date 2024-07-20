create table if not exists seq_log_audit
(
    id             varchar(32)                        not null comment 'id'
        primary key,
    url            varchar(255)                       null comment '名称',
    ip             varchar(32)                        null comment '实例数',
    user_name      varchar(32)                        null comment '用户名',
    user_id        varchar(32)                        null comment '用户id',
    method         varchar(32)                        null comment '方法名',
    audit_info     varchar(225)                       null comment '审计code',
    functions      varchar(225)                       null comment '操作内容',
    tenant_id      varchar(32)                        null comment '租户id',
    create_user_id varchar(32)                        null comment '新增用户',
    update_user_id varchar(32)                        null comment '修改用户',
    create_date    datetime default CURRENT_TIMESTAMP not null comment '新增时间',
    update_date    datetime default CURRENT_TIMESTAMP not null comment '修改时间'
)
    
    ;

create table if not exists seq_multicenter_cluster
(
    id             varchar(32)  not null
        primary key,
    create_date    timestamp    null,
    update_date    timestamp    null,
    update_user_id varchar(32)  null,
    create_user_id varchar(32)  null,
    tenant_id      varchar(32)  null comment '租户id',
    name           varchar(32)  null comment '名称',
    org_id         varchar(32)  null comment '组织',
    description    varchar(225) null comment '描述'
)
    
    ;

create table if not exists seq_multicenter_node
(
    id               varchar(32)                not null
        primary key,
    create_date      timestamp                  null,
    update_date      timestamp                  null,
    update_user_id   varchar(32)                null,
    create_user_id   varchar(32)                null,
    tenant_id        varchar(32)                null comment '租户id',
    seq_cluster_id   varchar(32)                null comment '序列服务集群id',
    multi_cluster_id varchar(32)                null comment '多中心id',
    sync_method      int         default 0      null comment '同步方式，0：冷备，1：热备',
    machine_room_id  varchar(32)                null comment '机房id',
    city_id          varchar(32)                null comment '城市id',
    data_type        int                        null comment '数据类型，0：主，1：备',
    status           varchar(32) default 'STOP' null comment '状态',
    syncTask_id      varchar(32)                null comment '同步任务id',
    current_db       varchar(32)                null comment '当前数据源，0：当前集群，1：备用集群',
    db_url           varchar(255)               null,
    db_driver        varchar(255)               null,
    db_user          varchar(255)               null,
    db_password      varchar(255)               null
)
    
    ;

create table if not exists seq_sdk_monitor
(
    id             varchar(32) charset utf8 default '唯一标识'        not null comment 'id'
        primary key,
    service_name   varchar(255) charset utf8                          null comment '服务名称',
    host_ip        varchar(32) charset utf8                           null comment 'sdk服务ip',
    port           varchar(10) charset utf8                           null comment 'sdk服务端口',
    host_name      varchar(255) charset utf8                          null comment 'sdk服务端口',
    status         varchar(2) charset utf8                            null comment 'sdk状态(0:停止，1：运行，2：故障)',
    service_id     varchar(32) charset utf8                           null comment '服务唯一标识',
    tenant_id      varchar(32) charset utf8                           null comment '租户id',
    create_user_id varchar(32) charset utf8                           null comment '新增用户',
    update_user_id varchar(32) charset utf8                           null comment '修改用户',
    create_date    datetime                 default CURRENT_TIMESTAMP not null comment '新增时间',
    update_date    datetime                 default CURRENT_TIMESTAMP not null comment '修改时间',
    context_path   varchar(255) charset utf8                          null,
    sdk_name       varchar(255)                                       null,
    work_id        int(4)                                             null comment '雪花算法中workId',
    cluster_name   varchar(255)                                       null comment '指定集群，1.配置具体集群信息；2未配置时，为ALL',
    version        int(4)                                             null comment '版本',
    instance_name  varchar(255)                                       null comment '实例名称',
    constraint UK_CLUSTER_WORD_ID
        unique (work_id, cluster_name) comment '唯一键约束，防止重复',
    constraint UK_WORK_SDK
        unique (sdk_name, work_id)
)
    
    ;

create table if not exists seq_service_cluster
(
    id               varchar(32) charset utf8             not null comment 'id'
        primary key,
    name             varchar(255) charset utf8            null comment '名称',
    status           varchar(2) charset utf8              null comment '状态(0:停止，1：运行，2：故障)',
    org_id           varchar(32) charset utf8             null comment '组织',
    city_id          varchar(32) charset utf8             null comment '城市',
    room_id          varchar(32) charset utf8             null comment '机房',
    unit_id          varchar(32)                          null comment '单元化id',
    tenant_id        varchar(32) charset utf8             null comment '租户id',
    create_user_id   varchar(32) charset utf8             null comment '新增用户',
    update_user_id   varchar(32) charset utf8             null comment '修改用户',
    create_date      datetime   default CURRENT_TIMESTAMP not null comment '新增时间',
    update_date      datetime   default CURRENT_TIMESTAMP not null comment '修改时间',
    db_type          varchar(32)                          null,
    db_url           varchar(255)                         null,
    db_driver        varchar(255)                         null,
    db_user          varchar(255)                         null,
    db_password      varchar(255)                         null,
    unit_type        varchar(10)                          null comment '单元类型',
    db_id            varchar(40)                          null,
    deploy_mode      varchar(2) default '2'               null comment '集群部署模式 ： 1:容器部署  2:虚机部署',
    k8s_cluster_name varchar(20)                          null comment 'k8s集群名称',
    namespace        varchar(20)                          null comment 'k8s命名空间',
    applications     varchar(2000)                        null comment '集群关联应用'
)
    
    ;

create table if not exists seq_service_node
(
    id             varchar(32) charset utf8                            not null comment 'id'
        primary key,
    machine_id     varchar(255) charset utf8                           null comment '名称',
    host_ip        varchar(32) charset utf8                            null,
    port           int(10)                                             null comment '实例数',
    deploy_path    varchar(2000) charset utf8                          null comment '状态(0:停止，1：运行，2：故障)',
    status         varchar(2) charset utf8                             null comment '状态(0:停止，1：运行，2：故障)',
    cluster_id     varchar(32) charset utf8                            null comment '服务集群id',
    tenant_id      varchar(32) charset utf8                            null comment '租户id',
    create_user_id varchar(32) charset utf8                            null comment '新增用户',
    update_user_id varchar(32) charset utf8                            null comment '修改用户',
    create_date    datetime                  default CURRENT_TIMESTAMP not null comment '新增时间',
    update_date    datetime                  default CURRENT_TIMESTAMP not null comment '修改时间',
    config_path    varchar(300) charset utf8 default ''                null comment '配置文件路径',
    logfile_path   varchar(300) charset utf8                           null comment '日志文件路径',
    unit_id        varchar(32)                                         null comment '单元id',
    unit_type      varchar(10)                                         null comment '单元类型',
    jmx_port       int(10)                                             null comment '监控端口',
    jmx_status     varchar(1)                                          null comment '监控运行状态(0:停止，1：运行，2：故障)'
)
    
    ;

create table if not exists seq_service_register
(
    id               varchar(32)  not null comment '唯一id'
        primary key,
    application_name varchar(225) null comment '序列应用名',
    host_ip          varchar(15)  null comment '服务ip',
    port             varchar(10)  null comment '服务端口',
    create_date      datetime     null comment '新增时间',
    update_date      datetime     null comment '修改时间'
)
    
    ;

