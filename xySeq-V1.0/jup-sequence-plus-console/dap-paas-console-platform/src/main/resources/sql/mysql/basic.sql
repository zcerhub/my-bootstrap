create table if not exists base_sys_app
(
    id                       varchar(32)  not null,
    name                     varchar(224) null comment '应用名称',
    code                     varchar(32)  null comment '应用编码',
    create_user_id           varchar(64)  null comment '创建人',
    create_date              datetime     null comment '创建时间',
    update_date              datetime     null comment '更新时间',
    update_user_id           varchar(64)  null comment '更新时间',
    description              varchar(200) null comment '描述',
    sort                     decimal(11)  null comment '排序',
    icon                     varchar(128) null comment '图标',
    tenant_id                varchar(32)  not null,
    product_instruction_book longblob     null,
    instruction_book_name    varchar(255) null,
    primary key (id, tenant_id)
)

;

create table if not exists base_sys_data_rule
(
    id             varchar(32)  not null
        primary key,
    update_date    datetime     null,
    update_user_id varchar(64)  null,
    create_date    datetime     null,
    create_user_id varchar(64)  null,
    name           varchar(32)  null comment '名称',
    is_include     varchar(1)   null comment '是否包含下级',
    menu_id        varchar(32)  null comment '所属菜单id',
    code           varchar(64)  null comment '编码',
    path           varchar(200) null comment '资源路径',
    rule           varchar(256) null comment '规则',
    type           varchar(32)  null,
    mapper_id      varchar(200) null comment 'mybatis的mapper方法id',
    description    varchar(255) null,
    app_id         varchar(32)  null,
    tenant_id      varchar(32)  null
)

;

create table if not exists base_sys_dictionary
(
    id             varchar(32)  not null,
    dic_code       varchar(60)  null comment '字典编码',
    dic_name       varchar(60)  null comment '字典名称',
    dic_desc       varchar(200) null comment '描述',
    cate_code      varchar(60)  null comment '所属分类',
    create_user_id varchar(64)  null comment '创建人',
    create_date    datetime     null comment '创建时间',
    update_user_id varchar(64)  null comment '更新人',
    update_date    datetime     null comment '更新时间',
    create_org_id  varchar(32)  null comment '所属机构',
    tenant_id      varchar(32)  not null,
    primary key (id, tenant_id)
)
comment '系统字典表'
;

create table if not exists base_sys_dictionary_info
(
    id             varchar(32)  not null,
    dict_id        varchar(60)  null comment '主表唯一标识',
    dic_name       varchar(128) null comment '字典名称',
    dic_value      varchar(128) null comment '字典值',
    dic_desc       varchar(200) null comment '字典描述',
    create_user_id varchar(64)  null comment '创建人',
    create_date    datetime     null comment '创建时间',
    update_user_id varchar(64)  null comment '更新人',
    update_date    datetime     null comment '更新时间',
    order_num      decimal(5)   null,
    tenant_id      varchar(32)  not null,
    primary key (id, tenant_id)
)
comment '字典详情'
;

create table if not exists base_sys_kubernetes_info
(
    id                     varchar(64)            not null
        primary key,
    cluster_name           varchar(50) default '' not null,
    container_cluster_name varchar(255)           null,
    type                   varchar(1)  default '' not null comment '1.mq_rocketmq 2.mq_kafka 3.redis',
    agent_ip               varchar(50) default '' not null,
    agent_port             int         default 0  not null,
    agent_state            varchar(1)             null comment '0:不可用，1:健康',
    images                 text                   null,
    create_user_id         varchar(64)            null,
    update_user_id         varchar(64)            null,
    create_date            datetime               null,
    update_date            datetime               null,
    tenant_id              varchar(255)           null,
    is_delete              varchar(1)             null
)

;

create table if not exists base_sys_log_audit
(
    id                 varchar(32)             not null comment '唯一ID'
        primary key,
    operator_user_id   varchar(64)             null comment '操作人员id',
    operator_user_name varchar(64)             null comment '操作人员名字',
    create_user_id     varchar(64)             null comment '创建用户id',
    menu_name          varchar(100)            null comment '菜单名称',
    operate_result     varchar(32) default '0' null comment '操作结果：0-成功，1-失败',
    operate_ip         varchar(100)            null comment '操作ip',
    button_name        varchar(255)            null comment '按钮名称',
    methon_name        varchar(225)            null comment '方法名称',
    description        varchar(225)            null comment '描述信息',
    create_date        datetime                null comment '操作时间',
    tenant_id          varchar(32)             null comment '租户id',
    component_type     varchar(255)            null comment '模块类型'
)
comment '日志审计表'
;

create table if not exists base_sys_menu
(
    id             varchar(32)  not null,
    update_date    datetime     null,
    update_user_id varchar(64)  null,
    create_date    datetime     null,
    create_user_id varchar(64)  null,
    text           varchar(200) null comment '菜单文本',
    path           varchar(200) null comment '资源路径',
    icon           varchar(200) null comment '图标',
    parent_id      varchar(32)  null comment '父菜单id',
    sort           decimal(11)  null comment '同级菜单排序',
    code           varchar(32)  null comment '编码',
    is_leaf        varchar(1)   null comment '是否是叶子节点,当该菜单下挂叶子节点的时候，此节点改为非叶子节点（y代表叶子节点,n代表非叶子节点）',
    route_path     varchar(200) null comment '前端路由',
    description    varchar(255) null,
    app_id         varchar(32)  null comment '应用id',
    app_name       varchar(128) null comment '所属应用名称',
    tenant_id      varchar(32)  not null,
    primary key (id, tenant_id)
)

;

create table if not exists base_sys_operate
(
    id             varchar(32)  not null,
    update_date    datetime     null,
    update_user_id varchar(64)  null,
    create_date    datetime     null,
    create_user_id varchar(64)  null,
    description    varchar(200) null,
    code           varchar(64)  null comment '操作编码',
    path           varchar(200) null comment '资源路径',
    name           varchar(64)  null comment '操作名称',
    menu_id        varchar(32)  null comment '所属菜单id',
    type           varchar(32)  null comment '请求类型:psot,get,put,delete',
    is_hidden      varchar(1)   null comment '按钮是否隐藏（0代表隐藏，1代表不隐藏）',
    tenant_id      varchar(32)  not null,
    primary key (id, tenant_id)
)

;

create table if not exists base_sys_org
(
    id             varchar(32)  not null
        primary key,
    parent_id      varchar(32)  null comment '上级机构id',
    name           varchar(224) null comment '机构名称',
    simple_name    varchar(64)  null comment '简写',
    code           varchar(32)  null comment '机构编码',
    type           varchar(2)   null comment '0:机构，1：组织，2:公司,3：项目，4：其他',
    status         varchar(2)   null comment '0:申请，1：经营，2：停业，3：撤销',
    sort           decimal(11)  null comment '排序',
    is_delete      varchar(2)   null comment '0:否（默认），1：是',
    description    varchar(200) null comment '描述',
    create_user_id varchar(64)  null comment '新增人',
    create_date    datetime     null comment '新增时间',
    update_date    datetime     null,
    update_user_id varchar(64)  null,
    tenant_id      varchar(32)  null
)
comment '组织机构'
;

create table if not exists base_sys_permission
(
    id             varchar(32) not null comment '唯一标识',
    update_date    datetime    null comment '更新时间',
    update_user_id varchar(32) null comment '更新人',
    create_user_id varchar(32) null comment '创建人',
    create_date    datetime    null comment '新增时间',
    menu_id        varchar(32) null comment '菜单id',
    role_id        varchar(32) null comment '角色id',
    operate_id     varchar(32) null comment '操作点id',
    app_id         varchar(32) null comment '应用id',
    tenant_id      varchar(32) not null,
    primary key (id, tenant_id)
)
comment '授权表'
;

create table if not exists base_sys_permission_data
(
    id             varchar(32) not null
        primary key,
    update_date    datetime    null,
    update_user_id varchar(32) null,
    create_user_id varchar(32) null,
    create_date    datetime    null,
    data_rule_id   varchar(32) null comment '数据表主键id',
    role_id        varchar(32) null comment '角色id',
    app_id         varchar(32) null,
    menu_id        varchar(32) null,
    tenant_id      varchar(32) null
)
comment '
'
;

create table if not exists base_sys_role
(
    id             varchar(32)  not null,
    update_date    datetime     null,
    update_user_id varchar(32)  null,
    create_user_id varchar(64)  null comment '新增人',
    create_date    datetime     null comment '新增时间',
    remark         varchar(32)  null comment '角色关键字',
    code           varchar(32)  null comment '角色编码',
    name           varchar(100) null comment '角色名称',
    description    varchar(200) null comment '角色描述',
    parent_id      varchar(32)  null comment '父角色id',
    status         varchar(1)   null comment '状态',
    type           varchar(32)  null comment '类型',
    role_sort      decimal(11)  null comment '排序',
    tenant_id      varchar(32)  not null,
    primary key (id, tenant_id)
)
comment '角色表'
;

create table if not exists base_sys_user
(
    id             varchar(32)  not null,
    account        varchar(32)  null comment '账户',
    sex            varchar(2)   null comment '0:男，1：女,3:保密',
    birthday       varchar(32)  null,
    name           varchar(32)  null,
    nick_name      varchar(32)  null,
    salt           varchar(16)  null,
    password       varchar(200) null,
    company_id     varchar(32)  null,
    dept_id        varchar(32)  null,
    email          varchar(32)  null,
    phone          varchar(32)  null,
    user_ip        varchar(64)  null,
    id_cast        varchar(64)  null,
    entry_date     varchar(32)  null comment '入职日期',
    status         varchar(1)   null comment '0：停用，1：启用',
    is_delete      varchar(1)   null comment '0：否，1：是',
    address        varchar(100) null,
    create_user_id varchar(64)  null comment '新增人',
    create_date    datetime     null comment '新增时间',
    update_date    datetime     null,
    update_user_id varchar(64)  null,
    theme          varchar(32)  null comment '用户主题',
    tenant_id      varchar(32)  not null,
    primary key (id, tenant_id)
)

;

create table if not exists base_sys_user_org
(
    id             varchar(32) not null,
    org_id         varchar(32) null,
    user_id        varchar(32) null,
    create_user_id varchar(64) null comment '新增人',
    create_date    datetime    null comment '新增时间',
    update_date    datetime    null,
    update_user_id varchar(64) null,
    tenant_id      varchar(64) not null,
    primary key (id, tenant_id)
)

;

create table if not exists base_sys_user_role
(
    id             varchar(32) not null,
    role_id        varchar(32) null comment '角色id',
    user_id        varchar(32) null comment '用户id',
    create_user_id varchar(64) null comment '新增人',
    create_date    datetime    null comment '新增时间',
    update_date    datetime    null,
    update_user_id varchar(64) null,
    tenant_id      varchar(32) not null,
    primary key (id, tenant_id)
)
comment '用户角色关联表'
;

create table if not exists basic_application
(
    id                varchar(32)            not null comment 'id'
        primary key,
    application_name  varchar(64)            not null comment '地区名称',
    application_code  varchar(64)            not null,
    organization_id   bigint                 null,
    charge_user       varchar(64)            null,
    description       varchar(128)           null comment '描述',
    deleted           tinyint(1) default 0   null,
    create_user_id    varchar(64)            null comment '新增人',
    create_date       datetime               null comment '新增时间',
    update_date       datetime               null,
    update_user_id    varchar(64)            null,
    tenant_id         varchar(64)            null,
    access_key        varchar(20)            null comment '接口访问key',
    access_key_status varchar(2) default '0' null comment '接口访问key启用状态 ''1''-启用，''0''-禁用'
)

;

create table if not exists basic_city
(
    id             varchar(32)          not null comment 'id'
        primary key,
    city_name      varchar(64)          not null comment '地区名称',
    city_code      varchar(64)          not null comment '描述',
    deleted        tinyint(1) default 0 null comment '删除',
    create_user_id varchar(64)          null comment '新增人',
    create_date    datetime             null comment '新增时间',
    update_date    datetime             null comment '更新时间',
    update_user_id varchar(64)          null comment '更新人',
    tenant_id      varchar(64)          null comment '租户'
)

;

create table if not exists basic_clientoperate
(
    id             varchar(32)          not null comment 'id'
        primary key,
    client_id      varchar(64)          not null comment '客户端id',
    type           varchar(20)          not null comment '操作类型',
    log_id         varchar(64)          null comment '关联日志id',
    deleted        tinyint(1) default 0 null,
    create_user_id varchar(64)          null comment '新增人',
    create_date    datetime             null comment '新增时间',
    update_date    datetime             null,
    update_user_id varchar(64)          null,
    tenant_id      varchar(64)          null
)

;

create table if not exists basic_config_template
(
    id             varchar(32)                  not null
        primary key,
    create_date    datetime                     null,
    update_date    datetime                     null,
    update_user_id varchar(32)                  null,
    create_user_id varchar(32)                  null,
    component_type varchar(32)                  not null comment '一级分类：缓存中心，消息中心',
    module_type    varchar(32)                  not null comment '二级分类：redis，rocketmq serv',
    content        longtext                     null comment '模板内容',
    name           varchar(32)                  not null comment '模板名称',
    status         int         default 0        not null comment '0:启用; 1:未启用;',
    tenant_id      varchar(32) default '租户id' null,
    deleted        tinyint(1)  default 0        not null
)
comment '通用配置模板表'
;

create table if not exists basic_container_image
(
    id                varchar(32)   not null
        primary key,
    image_type        varchar(32)   not null comment '镜像类型',
    image_name        varchar(32)   not null comment '镜像名称',
    image_address     varchar(2000) not null comment '镜像地址',
    image_version     varchar(32)   not null comment '镜像版本',
    image_pull_secret varchar(32)   null comment '镜像版本',
    remark            varchar(128)  null comment '镜像描述',
    create_user_id    varchar(64)   null,
    update_user_id    varchar(64)   null,
    create_date       datetime      null,
    update_date       datetime      null,
    tenant_id         varchar(255)  null,
    component_version varchar(64)   null comment '镜像内部组件版本',
    constraint image_name
        unique (image_name)
)
comment '镜像基本信息管理'
;

create table if not exists basic_deployclient
(
    id             varchar(32)          not null comment 'id'
        primary key,
    name           varchar(64)          not null comment '地区名称',
    host_address   varchar(20)          not null comment '描述',
    host_port      varchar(10)          null,
    remark         varchar(255)         null,
    pack_version   varchar(64)          null,
    state          tinyint(1)           null,
    deleted        tinyint(1) default 0 null,
    create_user_id varchar(64)          null comment '新增人',
    create_date    datetime             null comment '新增时间',
    update_date    datetime             null,
    update_user_id varchar(64)          null,
    tenant_id      varchar(64)          null
)

;

create table if not exists basic_filepackage
(
    id               varchar(32)          not null comment 'id'
        primary key,
    product_name     varchar(32)          not null comment '产品名称',
    package_version  varchar(32)          not null comment '版本',
    package_remark   varchar(64)          null comment '描述',
    package_name     varchar(32)          null comment '包名',
    sdk_name         varchar(32)          null comment 'SDK包',
    package_url      varchar(255)         null comment '存储路径',
    sdk_url          varchar(255)         null comment 'sdk存储路径',
    tablename        varchar(32)          null comment '类型选框',
    CPU_architecture varchar(32)          null comment 'CPU核心架构',
    create_date      datetime             null comment '新增时间',
    update_date      datetime             null,
    create_user_id   varchar(64)          null comment '新增人',
    update_user_id   varchar(64)          null,
    tenant_id        varchar(64)          null,
    deleted          tinyint(1) default 0 null
)

;

create table if not exists basic_machine
(
    id                varchar(32)            not null comment 'id'
        primary key,
    host_code         varchar(64)            null comment '主机编码',
    host_ip           varchar(32)            not null comment '机器ip',
    host_port         varchar(10)            not null comment '机器端口',
    host_ssh_account  varchar(64)            not null comment 'ssh账号',
    host_ssh_password varchar(32)            not null comment 'ssh密码',
    machine_room_id   varchar(32)            not null comment '所在机房id',
    os_release        varchar(64) default '' null comment '操作系统',
    os_version        varchar(64) default '' null comment '操作系统版本',
    host_remark       varchar(128)           null comment '备注',
    available         tinyint(1)  default 1  null comment '机器是否可用0；可用，1：不可用 ',
    core_arch         varchar(20)            null comment 'cpu内核',
    deleted           tinyint(1)  default 0  null comment '删除',
    create_user_id    varchar(64)            null comment '新增人',
    create_date       datetime               null comment '新增时间',
    update_date       datetime               null comment '更新时间',
    update_user_id    varchar(64)            null comment '更新人',
    tenant_id         varchar(64)            null comment '租户',
    deployment_path   varchar(255)           null comment '部署路径',
    unit_id           varchar(32)            null comment '单元id'
)

;

create table if not exists basic_machineroom
(
    id                varchar(32)          not null comment 'id'
        primary key,
    machine_room_code varchar(32)          not null comment '机房名称',
    machine_room_name varchar(64)          not null comment '所在地区id',
    organization_id   varchar(32)          null comment '组织编码',
    city_id           varchar(32)          not null comment '城市编码',
    description       varchar(128)         null comment '描述',
    deleted           tinyint(1) default 0 null comment '删除',
    create_user_id    varchar(64)          null comment '新增人',
    create_date       datetime             null comment '新增时间',
    update_date       datetime             null comment '更新时间',
    update_user_id    varchar(64)          null comment '更新人',
    tenant_id         varchar(64)          null comment '租户'
)

;

create table if not exists basic_organization
(
    id                varchar(32)          not null comment 'id'
        primary key,
    organization_code varchar(32)          not null comment '组织编码',
    organization_name varchar(64)          not null comment '组织名称',
    deleted           tinyint(1) default 0 null comment '删除标记',
    create_user_id    varchar(64)          null comment '新增人',
    create_date       datetime             null comment '新增时间',
    update_date       datetime             null comment '更新时间',
    update_user_id    varchar(64)          null comment '更新人',
    tenant_id         varchar(64)          null comment '租户'
)

;

create table if not exists basic_shelllog
(
    id             varchar(32)          not null comment 'id'
        primary key,
    status         varchar(64)          not null comment '状态',
    content        text       not null comment '描述',
    deleted        tinyint(1) default 0 null,
    create_user_id varchar(64)          null comment '新增人',
    create_date    datetime             null comment '新增时间',
    update_date    datetime             null,
    update_user_id varchar(64)          null,
    tenant_id      varchar(64)          null
)

;

create table if not exists basic_unit_info
(
    id              varchar(20)  not null comment '单元id'
        primary key,
    unit_code       varchar(20)  null comment '备份号',
    unit_name       varchar(200) null comment '单元名称',
    machine_room_id varchar(20)  null comment '机房id',
    unit_desc       varchar(200) null comment '备注',
    create_user_id  varchar(64)  null comment '新增人',
    create_date     datetime     null comment '新增时间',
    update_date     datetime     null,
    update_user_id  varchar(64)  null,
    tenant_id       varchar(64)  null,
    unit_type       varchar(1)   null comment '1-逻辑单元 2-全局单元 3-全局单元只读副本',
    bak_no          varchar(255) null
)

;

