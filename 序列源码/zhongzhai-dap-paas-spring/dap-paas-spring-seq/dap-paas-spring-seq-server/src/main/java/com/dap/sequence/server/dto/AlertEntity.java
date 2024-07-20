package com.dap.sequence.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author qqqab
 */
@ToString
@Data
@Builder
public  class AlertEntity {
    /**
     * 告警级别: Critical 3, Error 2, Warning 1, OK 0, Remind -1
     */
    @JsonProperty("severity")
    private Integer severity;
    /**
     * 告警名称
     */
    @JsonProperty("name")
    private String name;
    /**
     * 告警描述
     */
    @JsonProperty("description")
    private String description;
    /**
     * 发生时间
     */
    @JsonProperty("occur_time")
    private Long occurTime;
    /**
     * 告警对象
     */
    @JsonProperty("entity_name")
    private String entityName;
    /**
     * 告警对象地址
     */
    @JsonProperty("entity_addr")
    private String entityAddr;
    /**
     * 指定用于告警合并的字段，如果有多个字段请用逗号隔开。可选范围：entity_name,entity_addr,app_key,name,properties中的字段的code
     */
    @JsonProperty("merge_key")
    private String mergeKey;
    /**
     * 用于定位统一资源库的资源，如果有多个字段请用逗号隔开。可选范围：entity_name,entity_addr,properties中的字段的code
     */
    @JsonProperty("identify_key")
    private String identifyKey;
    /**
     * 告警类型：event 事件告警，metric 指标告警，默认为event(不传或为空都可以)
     */
    @JsonProperty("type")
    private String type;
    /**
     * 网络域：defaultZone 为默认域的Code信息，为了更好的区分告警所属的网络域信息,此信息可以在ANT产品中查询，不填写的情况，默认把告警划分到默认域
     */
    @JsonProperty("network_domain")
    private String networkDomain;
    @JsonProperty("properties")
    private List<Property> properties;
    @Data
    static
    class Property {
        private String val;
        private String code;
        private String name;
    }
}