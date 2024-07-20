package com.dap.sequence.server.rules;

import com.dap.sequence.api.dto.NumberRuleInfo;
import com.dap.sequence.api.dto.Rule;
import com.dap.sequence.api.obj.RuleParams;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.server.core.AsyncTask;
import com.dap.sequence.server.utils.SpringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;


@ExtendWith(MockitoExtension.class)
class NumberRuleHandlerTest {

    @InjectMocks
    NumberRuleHandler ruleHandler;
    @Mock
    private ApplicationContext applicationContext;
    @Mock
    private  AsyncTask asyncTask;

    @BeforeEach
    public void setUp() {
        // 模拟Spring应用上下文
        Mockito.when(applicationContext.getBean(AsyncTask.class)).thenReturn(asyncTask);

        // 使用反射设置静态字段
        ReflectionTestUtils.setField(SpringUtils.class, "context", applicationContext);
    }
    @Test
    void generateRule() {
        SeqObj seqContext=new SeqObj();
        seqContext.setSequenceCode("dddd");
        Rule rule=new Rule();
        NumberRuleInfo numberRuleInfo = new NumberRuleInfo();
        numberRuleInfo.setNumberEnd(100L);
        numberRuleInfo.setNowValue(50L);
        numberRuleInfo.setNumberStep(2);
        numberRuleInfo.setNumberFormat("00000");
        numberRuleInfo.setNumberCircle(1);
        numberRuleInfo.setThreshold(20);
        numberRuleInfo.setNumberStart(1L);
        rule.setRuleData(numberRuleInfo);
        RuleParams ruleParams=new RuleParams();
        ruleHandler.generateRule(seqContext,rule,ruleParams);
    }
}