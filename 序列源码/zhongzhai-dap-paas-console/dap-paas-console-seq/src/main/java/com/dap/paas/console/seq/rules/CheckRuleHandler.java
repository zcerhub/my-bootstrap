package com.dap.paas.console.seq.rules;

import com.dap.paas.console.seq.entity.base.BaseRuleHandler;
import com.dap.paas.console.seq.entity.base.BaseRuleInfo;
import com.dap.paas.console.seq.entity.base.CheckRuleInfo;
import com.dap.paas.console.seq.util.SequenceUtil;
import org.apache.commons.lang.StringUtils;

/**
 * @className CheckRuleHandler
 * @description 校验规则测试
 * @author renle
 * @date 2023/10/24 10:20:18 
 * @version: V23.06
 */
public class CheckRuleHandler implements BaseRuleHandler {

    @Override
    public Object generateRule(String id, String message, BaseRuleInfo ruleInfo) {
        CheckRuleInfo checkRuleInfo = (CheckRuleInfo)ruleInfo;
        long randomLong = SequenceUtil.getRandomLong(2);
        String result = String.valueOf(randomLong);
        int length = checkRuleInfo.getCheckLength();
        if (result.length() >= length) {
            result = result.substring(0, length - 1);
        } else {
            // 使用0补位
            result = checkRuleInfo.getCheckPosition() == 0 ? StringUtils.leftPad(result, length, "0")
                    : StringUtils.rightPad(result, length, "0");
        }
        return result;
    }

    @Override
    public String getRuleName() {
        return "7";
    }
}
