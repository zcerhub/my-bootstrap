package com.dap.paas.console.basic.entity;

import com.base.api.entity.BaseEntity;
import lombok.Data;

@Data
public class FilePackage extends BaseEntity {

    /**
     * 产品名称  db_column：product_name
     */
    private String productName;
    /**
     * 版本  db_version：package_version
     */
    private String version;
    /**
     * 描述  db_column: package_remark
     */
    private String Remark;
    /**
     * 部署  db_column: package_name
     */
    private String packageName;
    /**
     * SDK包  db_column： SDK_name
     */
    private String sdkName;
    /**
     * 包路径  db_column: package_url
     */
    private String packageUrl;
    /**
     * SDK路径  db_column: sdk_url
     */
    private String sdkUrl;
    /**
     * table类型  db_column: tablename
     */
    private String tableName;
    /**
     * 内核   db_column: CPU_architecture
     */
    private String cpuArchitecture;
//    /**
//     * 包类型：部署包 package
//     *        SDK包 sdk
//     * db_column: package_type
//     */
//    private String packageType;
}
