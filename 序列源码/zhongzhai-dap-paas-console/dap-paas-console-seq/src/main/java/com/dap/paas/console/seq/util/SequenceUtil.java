package com.dap.paas.console.seq.util;

import com.alibaba.fastjson.JSONObject;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.AuthenticationUserDto;
import com.base.sys.utils.UserUtil;
import com.dap.paas.console.seq.entity.SeqDesignPo;
import com.dap.paas.console.seq.entity.SeqInstanceRule;
import com.dap.paas.console.seq.entity.base.BaseRuleHandler;
import com.dap.paas.console.seq.entity.base.CheckRuleInfo;
import com.dap.paas.console.seq.entity.base.DateRuleInfo;
import com.dap.paas.console.seq.entity.base.EnumRuleInfo;
import com.dap.paas.console.seq.entity.base.NumberRuleInfo;
import com.dap.paas.console.seq.entity.base.OptionalRuleInfo;
import com.dap.paas.console.seq.entity.base.Rule;
import com.dap.paas.console.seq.entity.base.SpecialCharRuleInfo;
import com.dap.paas.console.seq.entity.base.StringRuleInfo;
import com.dap.paas.console.seq.holder.SeqHolder;
import com.dap.paas.console.seq.service.SeqDesignService;
import com.dap.paas.console.seq.service.SeqInstanceRuleService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
 * @className SequenceUtil
 * @description 序列工具类
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public class SequenceUtil {

    private static final Logger LOG = LoggerFactory.getLogger(SequenceUtil.class);

    private final SeqDesignService seqDesignService;

    private final SeqInstanceRuleService seqInstanceRuleService;

    public SequenceUtil(SeqDesignService seqDesignService, SeqInstanceRuleService seqInstanceRuleService) {
        this.seqDesignService = seqDesignService;
        this.seqInstanceRuleService = seqInstanceRuleService;
    }

    /**
     * 获取测试序列
     *
     * @param seqNo seqNo
     * @return Result
     */
    public Result test(String seqNo) {
        Map<String, String> map = Collections.singletonMap("seqCode", seqNo);
        SeqDesignPo seqDesignPo = seqDesignService.getObjectByCode(map);
        LOG.info("加载配置信息 = {}!!", seqDesignPo);
        String seq = Optional.ofNullable(seqDesignPo).map(po -> {
            doConfigRule(seqDesignPo);
            return this.testSequence(seqDesignPo);
        }).orElse("");
        return ResultUtils.success(seq);
    }


    /**
     * 填充序列生成规则信息
     *
     * @param seqDesignPo seqDesignPo
     */
    public void doConfigRule(SeqDesignPo seqDesignPo) {
        List<Rule> ruleInfos = seqDesignPo.getRuleInfos();

        // 查询  列表+
        Map<String, String> map = Collections.singletonMap("seqDesignId", seqDesignPo.getId());
        List<SeqInstanceRule> collect = seqInstanceRuleService.queryList(map);

        // 填充seqDesignPo的规则名
        List<String> ruleNameList = new ArrayList<>(collect.size());
        for (SeqInstanceRule e : collect) {
            Rule rule = new Rule();
            rule.setRuleName(e.getInstanceRuleName());
            rule.setRuleType(e.getInstanceRuleType());
            switch (e.getInstanceRuleType()) {
                default:
                    // 类型不匹配
                    throw new InstantiationError("序列类型不匹配!");
                case "1":
                    // 固定字符串
                    StringRuleInfo stringRuleInfo = JSONObject.parseObject(e.getInstanceRuleInfo(), StringRuleInfo.class);
                    rule.setRuleData(stringRuleInfo);
                    ruleInfos.add(rule);
                    break;
                case "2":
                    // 枚举
                    JSONObject jsonObject = JSONObject.parseObject(e.getInstanceRuleInfo());
                    EnumRuleInfo enumRuleInfo = new EnumRuleInfo(jsonObject.getInteger("numberStep"), jsonObject.getString("initData"), jsonObject.getString("enumStore"));
                    rule.setRuleData(enumRuleInfo);
                    ruleInfos.add(rule);
                    break;
                case "3":
                    // 日期
                    DateRuleInfo dateRuleInfo = JSONObject.parseObject(e.getInstanceRuleInfo(), DateRuleInfo.class);
                    rule.setRuleData(dateRuleInfo);
                    ruleInfos.add(rule);
                    break;
                case "4":
                    // 数字
                    NumberRuleInfo numberRuleInfo = JSONObject.parseObject(e.getInstanceRuleInfo(), NumberRuleInfo.class);
                    rule.setRuleData(numberRuleInfo);
                    ruleInfos.add(rule);
                    break;
                case "5":
                    // 占位符
                    SpecialCharRuleInfo specialCharRuleInfo = JSONObject.parseObject(e.getInstanceRuleInfo(), SpecialCharRuleInfo.class);
                    rule.setRuleData(specialCharRuleInfo);
                    ruleInfos.add(rule);
                    break;
                case "6":
                    // 自选序列
                    OptionalRuleInfo optionalRuleInfo = JSONObject.parseObject(e.getInstanceRuleInfo(), OptionalRuleInfo.class);
                    rule.setRuleData(optionalRuleInfo);
                    ruleInfos.add(rule);
                    break;

                case "7":
                    // 校验位
                    CheckRuleInfo checkRuleInfo = JSONObject.parseObject(e.getInstanceRuleInfo(), CheckRuleInfo.class);
                    rule.setRuleData(checkRuleInfo);
                    ruleInfos.add(rule);
                    break;
            }
            ruleNameList.add(e.getInstanceRuleName());
        }
        seqDesignPo.setSequenceRule(String.join(";", ruleNameList));
    }

    /**
     * 测试序列生产
     *
     * @param seqDesignPo seqDesignPo
     * @return String
     */
    public String testSequence(SeqDesignPo seqDesignPo) {
        List<Rule> rules = seqDesignPo.getRuleInfos();
        if (CollectionUtils.isNotEmpty(rules)) {
            StringBuffer seq = new StringBuffer();
            for (Rule rule : rules) {
                BaseRuleHandler ruleHandler = SeqHolder.getRuleHandlerByName(rule.getRuleType());
                Optional.ofNullable(ruleHandler).ifPresent(handler -> seq.append(handler.generateRule(seqDesignPo.getId(), seq.toString(), rule.getRuleData())));
            }
            LOG.info(" test result (seq)={}", seq);
            return seq.toString();
        }
        return null;
    }

    /**
     * 生成随机数
     *
     * @param length length
     * @return long
     */
    public static long getRandomLong(int length) {
        // 补位最大值
        Double paddindMaxVal = Math.pow(10, length);
        Random random = new Random();
        Double s = random.nextLong() % paddindMaxVal;
        return Math.abs(s.longValue());
    }

    /**
     * 获取租户id
     *
     * @return String
     */
    public static Optional<String> getTenantId() {
        return Optional.ofNullable(UserUtil.getUser()).map(AuthenticationUserDto::getTenantId);
    }
}
