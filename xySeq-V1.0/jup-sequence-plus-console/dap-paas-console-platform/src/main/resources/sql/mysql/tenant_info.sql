create table if not exists tenant_info
(
    id                varchar(32) not null
        primary key,
    tenant_name       varchar(64) null,
    tenant_company    varchar(64) null,
    tenant_mail       varchar(64) null,
    tenant_phone      varchar(32) null,
    tenant_realm_name varchar(32) null,
    tenant_status     varchar(1)  null,
    auth_status       varchar(1)  null,
    create_time       date        null,
    tenant_code       varchar(64) not null
);

INSERT INTO tenant_info (id, tenant_name, tenant_company, tenant_mail, tenant_phone, tenant_realm_name, tenant_status, auth_status, create_time, tenant_code) VALUES ('1574137658806220812', 'phone', 'phone', 'phone', 'phone', null, '1', '1', '2023-04-27', 'phone');
INSERT INTO tenant_info (id, tenant_name, tenant_company, tenant_mail, tenant_phone, tenant_realm_name, tenant_status, auth_status, create_time, tenant_code) VALUES ('391312369558487040', 'paas', 'paas', 'paas', 'paas', '', '1', '1', '2022-08-15', 'paas');

