package com.dap.paas.console.basic.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BaseSpecDTO {

    private String app;

    private Integer capacity;

    private String centerclustertag;

    /**
     * 实例数量
     */
    private Integer size;

//    private String dc;
//
//    private String env;

    private String image;

    /**
     * 网络模式:默认NodePort
     */
    private String netmode;

    /**
     * 存储类
     */
    private String storageclass;

    /**
     * 存储卷大小PVC
     */
    private Integer storagesize;

    private Double cpu;

    private Integer memory;

    private String vip;

    private String monitorimage;

    private String proxyimage;

    private String proxysecret;

    private String realname;

    //sshd镜像
    private String sshdimage;

    //deploymode
    private String deploymode;

    private String version;

}
