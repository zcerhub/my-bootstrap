package com.dap.paas.console.seq.entity;

import com.base.api.entity.BaseEntity;
import com.dap.paas.console.seq.check.LegalValue;
import com.dap.paas.console.seq.check.MultiFieldAssociation;
import com.dap.paas.console.seq.check.ValidGroup;
import com.dap.paas.console.seq.entity.base.Rule;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @className SeqDesignPo
 * @description 序列配置
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Data
//@MultiFieldAssociation.List(
//        value = {
//                @MultiFieldAssociation(field = "requestNumber", when = "#sequenceNumber != null", must = "#requestNumber != null && new Integer(#requestNumber) < new Integer(#sequenceNumber)", message = "请求数量应该小于缓存数量")
//        }
//)
public class SeqDesignPo extends BaseEntity {

    /**
     * 序列名称
     */
    @NotBlank(message = "序列名称不能为空！", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    @Size(min = 1, max = 100, message = "序列名称长度范围为1~100", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    private String sequenceName;

    /**
     *序列编号
     */
    @NotBlank(message = "序列编号不能为空！", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    @Size(min = 1, max = 255, message = "序列编号长度范围为1~255", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    private String sequenceCode;

    /**
     *应用id
     */
    @NotBlank(message = "序列应用id不能为空！", groups = {ValidGroup.Valid.SeqDesignIssue.class})
    @Size(min = 1, max = 50, message = "序列应用id长度范围为1~50", groups = {ValidGroup.Valid.SeqDesignIssue.class})
    private String sequenceApplicationId;

    /**
     *描述
     */
    private String sequenceDesc;

    /**
     * 应用名称
     */
    @NotBlank(message = "应用名称不能为空！", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    @Size(min = 1, max = 100, message = "应用名称长度范围为1~100", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    private String sequenceApplicationName;

    /**
     * 缓存数量
     */
    @NotBlank(message = "缓存数量不能为空！", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    @Range(min = 0, max = 50000, message = "缓存数量范围在0~50000", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    private String sequenceNumber;

    /**
     * 请求数量
     */
    @NotBlank(message = "请求数量不能为空！", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    @Range(min = 1, max = 50000, message = "请求数量范围在1~50000", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    private String requestNumber;

    /**
     * 服务缓存阈值
     */
    @NotBlank(message = "请求数量不能为空！", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    @Range(min = 1, max = 99, message = "服务端缓存阈值范围在1~99", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    private String serverCacheThreshold;

    /**
     * 客户缓存阈值
     */
    @NotBlank(message = "请求数量不能为空！", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    @Range(min = 1, max = 99, message = "客户端缓存阈值范围在1~99", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    private String clientCacheThreshold;

    /**
     * 状态  1未完成 2完成
     */
    @LegalValue(values = {"0", "1"}, message = "服务端缓存回收开关")
    private String serverRecoverySwitch;

    /**
     * 状态  1未完成 2完成
     */
    @LegalValue(values = {"0", "1"}, message = "客户端缓存回收开关")
    private String clientRecoverySwitch;

    /**
     * 状态  1未完成 2完成
     */
    @LegalValue(values = {"1", "2"}, message = "状态参数非法")
    private String sequenceStatus;

    /**
     * 规则
     */
    private String sequenceRule;

    /**
     * 序列规则对象链表
     */
    private List<Rule> ruleInfos = new ArrayList<>();

    /**
     * 回拨模式  0 全局序列 1：按日回拨 （到下一日时序号中数字从头开始）  2：按月回拨 3：按年回拨
     */
    @LegalValue(values = {"0", "1", "2", "3"}, message = "回拨模式非法", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    private String callbackMode;

    /**  appcode */
    private String appCode;

    /**  accesskey */
    private String accessKey;

}
