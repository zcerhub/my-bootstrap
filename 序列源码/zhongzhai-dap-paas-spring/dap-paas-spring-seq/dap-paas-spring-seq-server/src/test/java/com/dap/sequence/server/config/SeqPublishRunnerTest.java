package com.dap.sequence.server.config;

import com.alibaba.fastjson.JSONObject;
import com.dap.sequence.api.dto.CheckRuleInfo;
import com.dap.sequence.api.dto.DateRuleInfo;
import com.dap.sequence.api.dto.EnumRuleInfo;
import com.dap.sequence.api.dto.NumberRuleInfo;
import com.dap.sequence.api.dto.OptionalRuleInfo;
import com.dap.sequence.api.dto.SpecialCharRuleInfo;
import com.dap.sequence.api.dto.StringRuleInfo;
import com.dap.sequence.server.core.AsyncTask;
import com.dap.sequence.server.entity.SeqDesignPo;
import com.dap.sequence.server.entity.SeqInstanceRule;
import com.dap.sequence.server.service.SeqDesignService;
import com.dap.sequence.server.service.SeqInstanceRuleService;
import com.dap.sequence.server.service.SeqPlatformService;
import com.dap.sequence.server.service.SeqSdkMonitorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * @className SeqPublishRunnerTest
 * @description TODO
 * @author renle
 * @date 2024/02/05 16:33:08 
 * @version: V23.06
 */
@RunWith(MockitoJUnitRunner.class)
public class SeqPublishRunnerTest {

    @InjectMocks
    private SeqPublishRunner seqPublishRunner;

    @Spy
    private SeqInstanceRuleService seqInstanceRuleService;

    @Spy
    private SeqDesignService seqDesignService;

    @Spy
    private SeqPlatformService seqFromPlatformService;

    @Spy
    private AsyncTask asyncTask;

    @Spy
    private SeqSdkMonitorService seqSdkMonitorService;

    @Test
    public void run() throws Exception {
        List<SeqInstanceRule> seqInstanceRules = new ArrayList<SeqInstanceRule>() {
            {
                add(new SeqInstanceRule() {
                    {
                        setSeqDesignId("1");
                        setInstanceRuleType("1");
                        setInstanceRuleInfo(JSONObject.toJSONString(new StringRuleInfo()));
                    }
                });
                add(new SeqInstanceRule() {
                    {
                        setSeqDesignId("1");
                        setInstanceRuleType("2");
                        setInstanceRuleInfo(JSONObject.toJSONString(new EnumRuleInfo() {
                            {
                                setEnumStore(new ArrayList<String>() {
                                    {
                                        add("1");
                                        add("2");
                                    }
                                });
                                setInitData("1");
                            }
                        }));
                    }
                });
                add(new SeqInstanceRule() {
                    {
                        setSeqDesignId("1");
                        setInstanceRuleType("3");
                        setInstanceRuleInfo(JSONObject.toJSONString(new DateRuleInfo()));
                    }
                });
                add(new SeqInstanceRule() {
                    {
                        setSeqDesignId("1");
                        setInstanceRuleType("4");
                        setInstanceRuleInfo(JSONObject.toJSONString(new NumberRuleInfo()));
                    }
                });
                add(new SeqInstanceRule() {
                    {
                        setSeqDesignId("1");
                        setInstanceRuleType("5");
                        setInstanceRuleInfo(JSONObject.toJSONString(new SpecialCharRuleInfo()));
                    }
                });
                add(new SeqInstanceRule() {
                    {
                        setSeqDesignId("1");
                        setInstanceRuleType("6");
                        setInstanceRuleInfo(JSONObject.toJSONString(new OptionalRuleInfo()));
                    }
                });
                add(new SeqInstanceRule() {
                    {
                        setSeqDesignId("1");
                        setInstanceRuleType("7");
                        setInstanceRuleInfo(JSONObject.toJSONString(new CheckRuleInfo()));
                    }
                });
                add(new SeqInstanceRule() {
                    {
                        setSeqDesignId("1");
                        setInstanceRuleType("8");
                        setInstanceRuleInfo(JSONObject.toJSONString(new EnumRuleInfo()));
                    }
                });
            }
        };
        when(seqInstanceRuleService.queryList(Mockito.anyMap())).thenReturn(seqInstanceRules);
        SeqPublishRunner publishRunner = Mockito.spy(seqPublishRunner);
        doNothing().when(publishRunner).updateSeqHolder();
        when(seqSdkMonitorService.rcvSnowflakeWorkId(Mockito.anyInt())).thenReturn(1);
        publishRunner.run(null);
    }

    @Test
    public void updateSeqHolder() {
        List<SeqDesignPo> seqDesignPoList = new ArrayList<>();
        when(seqDesignService.queryList(Mockito.anyMap())).thenReturn(seqDesignPoList);
        seqPublishRunner.updateSeqHolder();
    }

    @Test
    public void cacheSequence() {
        List<SeqDesignPo> seqDesignPoList = new ArrayList<SeqDesignPo>() {
            {
                {
                    add(new SeqDesignPo() {
                        {
                            setSequenceCode("1");
                            setSequenceNumber(10);
                        }
                    });
                }
            }
        };
        when(seqDesignService.queryList(Mockito.anyMap())).thenReturn(seqDesignPoList);
        seqPublishRunner.cacheSequence();
    }
}