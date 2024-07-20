create table if not exists job_attribute_config
(
    id              int auto_increment comment 'id'
        primary key,
    attribute_value varchar(200)  null comment '属性值',
    attribute_id    int                           null
)
comment '属性配置表'
;

create table if not exists job_attribute_config_def
(
    id                 int auto_increment comment 'id'
        primary key,
    attribute_classify varchar(32)  null comment '分类 D:自定义，S:固定',
    attribute_group    varchar(32)  null comment '分组',
    attribute_type     varchar(32)  null comment '属性类型 S-字符型',
    attribute_name     varchar(64)  null comment '属性名',
    attribute_code     varchar(64)  null comment '属性',
    constraint job_attribute_config_def_uni_idx
        unique (attribute_code)
)
comment '属性配置定义表'
;

create table if not exists job_data_source
(
    data_source_id             bigint auto_increment comment '数据源id'
        primary key,
    data_source_name           varchar(200)             not null comment '数据源名称',
    data_source_driver         varchar(200)             not null comment '数据源驱动',
    data_source_address        varchar(500)             not null comment '数据源地址',
    data_source_user           varchar(50)              null comment '数据源用户名',
    data_source_password       varchar(1000)            null comment '密码',
    data_source_connect_status varchar(10)  default '0' null comment '数据源连接状态（1-已连接；0-未连接；）',
    data_source_status         varchar(10)  default '1' null comment '数据源记录状态（1-正常；0-失效）',
    data_source_create_time    timestamp                                null comment '创建时间',
    data_source_update_time    timestamp                                null on update CURRENT_TIMESTAMP comment '更新时间'
)

;

create table if not exists job_demo
(
    job_user_id   varchar(200)  not null
        primary key,
    job_user_name varchar(200)  null,
    job_amt_01    decimal                       null,
    job_amt_02    decimal                       null,
    job_order     varchar(200)  null
)

;

create table if not exists job_distributed_lock
(
    job_lock_id       bigint auto_increment comment '主键'
        primary key,
    job_lock_name     varchar(200)  not null comment '锁名称',
    job_lock_desc     varchar(100)  null comment '描述',
    job_host          varchar(200)  null comment '执行主机信息',
    job_uuid          varchar(200)  null comment 'UUID重入锁时使用该字段判断',
    job_create_time   timestamp                     null on update CURRENT_TIMESTAMP comment '创建时间',
    job_lock_time_out bigint default 0              null comment '锁超时时间',
    constraint index1
        unique (job_lock_name)
)

;

create table if not exists job_excute_batch
(
    job_batch_id      bigint auto_increment comment '主键'
        primary key,
    job_execute_batch varchar(200)   null comment '执行主批次',
    logic_code        varchar(50)    null comment '服务逻辑分片编码',
    reg_code          varchar(100)   null comment '作业注册中心',
    job_group_code    varchar(50)    null,
    job_code          varchar(50)    null,
    job_host          varchar(200)   null,
    job_shardingitem  varchar(1024)  null,
    job_message       text           null,
    job_status        varchar(10)    null,
    job_starttime     timestamp                      null,
    job_completetime  timestamp                      null
)

;

create index idx1
    on job_excute_batch (job_execute_batch, logic_code, job_code);

create index idx_excute_batch
    on job_excute_batch (job_group_code, job_code);

create table if not exists job_qrtz_blob_triggers
(
    SCHED_NAME    varchar(120) not null,
    TRIGGER_NAME  varchar(190) not null,
    TRIGGER_GROUP varchar(190) not null,
    BLOB_DATA     blob         null,
    primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
)

;

create index SCHED_NAME
    on job_qrtz_blob_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

create table if not exists job_qrtz_calendars
(
    SCHED_NAME    varchar(120) not null,
    CALENDAR_NAME varchar(190) not null,
    CALENDAR      blob         not null,
    primary key (SCHED_NAME, CALENDAR_NAME)
)

;

create table if not exists job_qrtz_cron_triggers
(
    SCHED_NAME      varchar(120) not null,
    TRIGGER_NAME    varchar(190) not null,
    TRIGGER_GROUP   varchar(190) not null,
    CRON_EXPRESSION varchar(120) not null,
    TIME_ZONE_ID    varchar(80)  null,
    primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
)

;

create table if not exists job_qrtz_fired_triggers
(
    SCHED_NAME        varchar(120) not null,
    ENTRY_ID          varchar(95)  not null,
    TRIGGER_NAME      varchar(190) not null,
    TRIGGER_GROUP     varchar(190) not null,
    INSTANCE_NAME     varchar(190) not null,
    FIRED_TIME        bigint       not null,
    SCHED_TIME        bigint       not null,
    PRIORITY          int          not null,
    STATE             varchar(16)  not null,
    JOB_NAME          varchar(190) null,
    JOB_GROUP         varchar(190) null,
    IS_NONCONCURRENT  varchar(1)   null,
    REQUESTS_RECOVERY varchar(1)   null,
    primary key (SCHED_NAME, ENTRY_ID)
)

;

create index IDX_JOB_QRTZ_FT_INST_JOB_REQ_RCVRY
    on job_qrtz_fired_triggers (SCHED_NAME, INSTANCE_NAME, REQUESTS_RECOVERY);

create index IDX_JOB_QRTZ_FT_JG
    on job_qrtz_fired_triggers (SCHED_NAME, JOB_GROUP);

create index IDX_JOB_QRTZ_FT_J_G
    on job_qrtz_fired_triggers (SCHED_NAME, JOB_NAME, JOB_GROUP);

create index IDX_JOB_QRTZ_FT_TG
    on job_qrtz_fired_triggers (SCHED_NAME, TRIGGER_GROUP);

create index IDX_JOB_QRTZ_FT_TRIG_INST_NAME
    on job_qrtz_fired_triggers (SCHED_NAME, INSTANCE_NAME);

create index IDX_JOB_QRTZ_FT_T_G
    on job_qrtz_fired_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

create table if not exists job_qrtz_job_details
(
    SCHED_NAME        varchar(120) not null,
    JOB_NAME          varchar(190) not null,
    JOB_GROUP         varchar(190) not null,
    DESCRIPTION       varchar(250) null,
    JOB_CLASS_NAME    varchar(250) not null,
    IS_DURABLE        varchar(1)   not null,
    IS_NONCONCURRENT  varchar(1)   not null,
    IS_UPDATE_DATA    varchar(1)   not null,
    REQUESTS_RECOVERY varchar(1)   not null,
    JOB_DATA          blob         null,
    primary key (SCHED_NAME, JOB_NAME, JOB_GROUP)
)

;

create index IDX_JOB_QRTZ_J_GRP
    on job_qrtz_job_details (SCHED_NAME, JOB_GROUP);

create index IDX_JOB_QRTZ_J_REQ_RECOVERY
    on job_qrtz_job_details (SCHED_NAME, REQUESTS_RECOVERY);

create table if not exists job_qrtz_locks
(
    SCHED_NAME varchar(120) not null,
    LOCK_NAME  varchar(40)  not null,
    primary key (SCHED_NAME, LOCK_NAME)
)

;

create table if not exists job_qrtz_paused_trigger_grps
(
    SCHED_NAME    varchar(120) not null,
    TRIGGER_GROUP varchar(190) not null,
    primary key (SCHED_NAME, TRIGGER_GROUP)
)

;

create table if not exists job_qrtz_scheduler_state
(
    SCHED_NAME        varchar(120) not null,
    INSTANCE_NAME     varchar(190) not null,
    LAST_CHECKIN_TIME bigint       not null,
    CHECKIN_INTERVAL  bigint       not null,
    primary key (SCHED_NAME, INSTANCE_NAME)
)

;

create table if not exists job_qrtz_simple_triggers
(
    SCHED_NAME      varchar(120) not null,
    TRIGGER_NAME    varchar(190) not null,
    TRIGGER_GROUP   varchar(190) not null,
    REPEAT_COUNT    bigint       not null,
    REPEAT_INTERVAL bigint       not null,
    TIMES_TRIGGERED bigint       not null,
    primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
)

;

create table if not exists job_qrtz_simprop_triggers
(
    SCHED_NAME    varchar(120)   not null,
    TRIGGER_NAME  varchar(190)   not null,
    TRIGGER_GROUP varchar(190)   not null,
    STR_PROP_1    varchar(512)   null,
    STR_PROP_2    varchar(512)   null,
    STR_PROP_3    varchar(512)   null,
    INT_PROP_1    int            null,
    INT_PROP_2    int            null,
    LONG_PROP_1   bigint         null,
    LONG_PROP_2   bigint         null,
    DEC_PROP_1    decimal(13, 4) null,
    DEC_PROP_2    decimal(13, 4) null,
    BOOL_PROP_1   varchar(1)     null,
    BOOL_PROP_2   varchar(1)     null,
    primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
)

;

create table if not exists job_qrtz_triggers
(
    SCHED_NAME     varchar(120) not null,
    TRIGGER_NAME   varchar(190) not null,
    TRIGGER_GROUP  varchar(190) not null,
    JOB_NAME       varchar(190) not null,
    JOB_GROUP      varchar(190) not null,
    DESCRIPTION    varchar(250) null,
    NEXT_FIRE_TIME bigint       null,
    PREV_FIRE_TIME bigint       null,
    PRIORITY       int          null,
    TRIGGER_STATE  varchar(16)  not null,
    TRIGGER_TYPE   varchar(8)   not null,
    START_TIME     bigint       not null,
    END_TIME       bigint       null,
    CALENDAR_NAME  varchar(190) null,
    MISFIRE_INSTR  smallint     null,
    JOB_DATA       blob         null,
    primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
)

;

create index IDX_JOB_QRTZ_T_C
    on job_qrtz_triggers (SCHED_NAME, CALENDAR_NAME);

create index IDX_JOB_QRTZ_T_G
    on job_qrtz_triggers (SCHED_NAME, TRIGGER_GROUP);

create index IDX_JOB_QRTZ_T_J
    on job_qrtz_triggers (SCHED_NAME, JOB_NAME, JOB_GROUP);

create index IDX_JOB_QRTZ_T_JG
    on job_qrtz_triggers (SCHED_NAME, JOB_GROUP);

create index IDX_JOB_QRTZ_T_NEXT_FIRE_TIME
    on job_qrtz_triggers (SCHED_NAME, NEXT_FIRE_TIME);

create index IDX_JOB_QRTZ_T_NFT_MISFIRE
    on job_qrtz_triggers (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME);

create index IDX_JOB_QRTZ_T_NFT_ST
    on job_qrtz_triggers (SCHED_NAME, TRIGGER_STATE, NEXT_FIRE_TIME);

create index IDX_JOB_QRTZ_T_NFT_ST_MISFIRE
    on job_qrtz_triggers (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME, TRIGGER_STATE);

create index IDX_JOB_QRTZ_T_NFT_ST_MISFIRE_GRP
    on job_qrtz_triggers (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME, TRIGGER_GROUP,
                          TRIGGER_STATE);

create index IDX_JOB_QRTZ_T_N_G_STATE
    on job_qrtz_triggers (SCHED_NAME, TRIGGER_GROUP, TRIGGER_STATE);

create index IDX_JOB_QRTZ_T_N_STATE
    on job_qrtz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP, TRIGGER_STATE);

create index IDX_JOB_QRTZ_T_STATE
    on job_qrtz_triggers (SCHED_NAME, TRIGGER_STATE);

