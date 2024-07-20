package com.dap.paas.console.seq.entity;


import com.base.api.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @className SeqSdkMonitor
 * @description 序列SDK监控实体
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Getter
@Setter
@NoArgsConstructor
public class SeqSdkMonitor extends BaseEntity {

    /**
     * 应用名称
     */
    private String serviceName;

    /**
     * sdk服务ip
     */
    private String hostIp;

    /**
     * sdk服务端口
     */
    private String port;

    /**
     * sdk服务状态
     */
    private String status;

    /**
     * 应用唯一标识
     */
    private String serviceId;

    private String sdkName;

//    public String getStatus() {
//        return status;
////        if (StringUtils.isNotBlank(status)) {
////            return status;
////        }
////        if (System.currentTimeMillis() - getUpdateDate().getTime() > 60 * 1000) {
////            return SeqConstant.SEQ_NODE_STATUS_STOP;
////        }
////        return SeqConstant.SEQ_NODE_STATUS_RUN;
//    }

    private String contextPath;

    /**
     * 应用所在虚拟或者容器的HOSTNAME
     */
    private String instanceName;

    /**
     * 序列的workdId
     */
    private Integer workId;

    /**
     * 数据版本，乐观锁
     */
    private Integer version;

}
