package com.dap.sequence.server.service.impl;

import com.dap.sequence.api.dto.EnumRuleInfo;
import com.dap.sequence.api.dto.NumberRuleInfo;
import com.dap.sequence.api.dto.Rule;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.obj.RuleParams;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.server.dao.SeqOptionalRecordDao;
import com.dap.sequence.server.entity.SeqCurNum;
import com.dap.sequence.server.entity.SeqOptionalPad;
import com.dap.sequence.server.entity.SeqOptionalRecord;
import com.dap.sequence.server.entity.SeqRecycleInfo;
import com.dap.sequence.server.service.SeqCurNumService;
import com.dap.sequence.server.service.SeqOptionalPadService;
import com.dap.sequence.server.service.SeqOptionalRecordService;
import com.dap.sequence.server.service.SeqRecycleInfoService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

/**
 * @className SeqNumServiceTest
 * @description TODO
 * @author renle
 * @date 2024/02/02 17:53:29 
 * @version: V23.06
 */
@RunWith(MockitoJUnitRunner.class)
public class SeqNumServiceTest {

    @InjectMocks
    private SeqNumService seqNumService;

    @Spy
    private SeqCurNumService seqCurNumService;

    @Spy
    private SeqRecycleInfoService seqRecycleInfoService;

    @Spy
    private SeqOptionalPadService seqOptionalPadService;

    @Spy
    private SeqOptionalRecordService seqOptionalRecordService;

    @Spy
    private SeqOptionalRecordDao seqOptionalRecordDao;


    @Test
    public void updateNum_01() {
        SeqNumService numService = Mockito.spy(seqNumService);
        SeqCurNum seqCurNum = new SeqCurNum() {
            {
                setCurVal("10");
            }
        };
        doReturn(seqCurNum).when(numService).getSeqCurNum(Mockito.any(), Mockito.any());
        when(seqCurNumService.selectForUpdateById(Mockito.any())).thenReturn(seqCurNum);
        Rule rule = new Rule() {
            {
                setRuleData(new NumberRuleInfo() {
                    {
                        setNumberStart(1L);
                        setNumberEnd(10L);
                        setNumberStep(1);
                        setOptional(true);
                        setNumberCircle(3);
                    }
                });
            }
        };
        SeqObj seqObj = new SeqObj();
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setCreateNumber(1);
            }
        };
        LinkedList<Long> linkedList = numService.updateNum(rule, seqObj, seqParamsDto);
        Assert.assertEquals(0, linkedList.size());
    }

    @Test
    public void updateNum_02() {
        SeqNumService numService = Mockito.spy(seqNumService);
        SeqCurNum seqCurNum = new SeqCurNum() {
            {
                setCurVal("10");
                setSeqLock(1);
            }
        };
        doReturn(seqCurNum).when(numService).getSeqCurNum(Mockito.any(), Mockito.any());
        when(seqCurNumService.selectForUpdateById(Mockito.any())).thenReturn(seqCurNum);
        when(seqCurNumService.updateObject(Mockito.any())).thenReturn(1);
        Rule rule = new Rule() {
            {
                setRuleData(new NumberRuleInfo() {
                    {
                        setNumberStart(1L);
                        setNumberEnd(10L);
                        setNumberStep(1);
                        setOptional(true);
                        setNumberCircle(0);
                    }
                });
            }
        };
        SeqObj seqObj = new SeqObj();
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setCreateNumber(1);
            }
        };
        LinkedList<Long> linkedList = numService.updateNum(rule, seqObj, seqParamsDto);
        Assert.assertEquals(1, linkedList.size());
    }

    @Test
    public void updateNumOptional() {
        SeqCurNum seqCurNum = new SeqCurNum() {
            {
                setCurVal("988");
            }
        };
        when(seqCurNumService.getObjectByCode(Mockito.anyMap())).thenReturn(null).thenReturn(seqCurNum);
        when(seqCurNumService.saveObject(Mockito.any())).thenReturn(0);
        SeqOptionalPad optionalPad = new SeqOptionalPad() {
            {
                setId("123");
                setOptionalValue(1888L);
                setPaddindValue(1L);
            }
        };
        when(seqOptionalPadService.getOptionalPad(Mockito.any(), Mockito.any())).thenReturn(optionalPad);
        //optionalPad = seqOptionalPadService.selectForUpdatePadById(optionalPad.getId());
        when(seqOptionalPadService.selectForUpdatePadById(Mockito.anyString())).thenReturn(optionalPad);
        Rule rule = new Rule() {
            {
                setRuleData(new NumberRuleInfo() {
                    {
                        setNumberStart(1L);
                        setNumberEnd(10000L);
                        setNumberStep(1);
                        setOptional(true);
                        setNumberCircle(0);
                    }
                });
            }
        };
        SeqObj seqObj = new SeqObj();
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setCreateNumber(1);
                setSeqVal("888");
            }
        };
        RuleParams ruleParams = new RuleParams();
        LinkedList<Long> linkedList = seqNumService.updateNumOptional(rule, seqObj, seqParamsDto, ruleParams);
        Assert.assertEquals(1, linkedList.size());
    }

    @Test
    public void updateNumOptional_01() {
        SeqCurNum seqCurNum = new SeqCurNum() {
            {
                setCurVal("988");
                setSeqLock(1);
            }
        };
        when(seqCurNumService.getObjectByCode(Mockito.anyMap())).thenReturn(null).thenReturn(seqCurNum);
        when(seqCurNumService.selectForUpdateById(Mockito.any())).thenReturn(seqCurNum);
        when(seqCurNumService.updateObject(Mockito.any())).thenReturn(1);
        Rule rule = new Rule() {
            {
                setRuleData(new NumberRuleInfo() {
                    {
                        setNumberStart(1L);
                        setNumberEnd(10000L);
                        setNumberStep(1);
                        setNumberCircle(0);
                        setOptional(false);
                    }
                });
            }
        };
        SeqObj seqObj = new SeqObj();
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setCreateNumber(1);
            }
        };
        RuleParams ruleParams = new RuleParams();
        LinkedList<Long> linkedList = seqNumService.updateNumOptional(rule, seqObj, seqParamsDto, ruleParams);
        Assert.assertEquals(1, linkedList.size());
    }

    @Test
    public void buildOptionalSeq() {
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        RuleParams ruleParams = new RuleParams();
        String seqNo = "123";
        SeqOptionalRecord seqOptionalRecord = seqNumService.buildOptionalSeq(seqParamsDto, ruleParams, seqNo);
        Assert.assertEquals("123", seqOptionalRecord.getSerialNumber());
    }

    @Test
    public void getSeqCurNum() {
        //SeqCurNum seqCurNum = new SeqCurNum();
        when(seqCurNumService.getObjectByCode(Mockito.any())).thenReturn(new SeqCurNum());
        SeqCurNum seqCurNum = seqNumService.getSeqCurNum(new SeqParamsDto() {
            {
                setDay("qqq");
            }
        }, new Rule());
        Assert.assertNotNull(seqCurNum);
    }

    @Test
    public void updateEnum() {
        when(seqCurNumService.getObjectByCode(Mockito.any())).thenReturn(new SeqCurNum());
        SeqCurNum seqCurNum = new SeqCurNum() {
            {
                setCurVal("123");
                setSeqLock(1);
            }
        };
        when(seqCurNumService.selectForUpdateById(Mockito.any())).thenReturn(seqCurNum);
        when(seqCurNumService.updateObject(Mockito.any())).thenReturn(1);
        Rule rule = new Rule() {
            {
                setRuleData(new EnumRuleInfo() {
                    {
                        setEnumStore(new ArrayList<String>() {
                            {
                                add("1");
                                add("2");
                                add("3");
                            }
                        });
                        setNumberStep(1);
                    }
                });
            }
        };
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setCreateNumber(10);
            }
        };
        seqNumService.updateEnum(rule, seqParamsDto);
        Assert.assertEquals("1", seqCurNum.getCurVal());
    }

    @Test
    public void addCallbackSeq() {
        List<SeqRecycleInfo> recycleInfos = new ArrayList<SeqRecycleInfo>() {
            {
                add(new SeqRecycleInfo() {
                    {
                        setRecycleCode("1");
                        setId("123");
                    }
                });
            }
        };
        when(seqRecycleInfoService.getSeqForUpdate(Mockito.any())).thenReturn(recycleInfos);
        when(seqRecycleInfoService.delObjectByIds(Mockito.any())).thenReturn(1);
        SeqObj seqObj = new SeqObj() {
            {
                setSequenceNumber(1);
            }
        };
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        LinkedBlockingDeque<Object> blockingQueue = new LinkedBlockingDeque<>();
        seqNumService.addCallbackSeq(seqObj, seqParamsDto, blockingQueue);
        Assert.assertEquals(1, blockingQueue.size());
    }

    @Test
    public void saveBatchOptional() {
        when(seqOptionalRecordDao.saveBatchOptional(Mockito.any())).thenReturn(1);
        seqNumService.saveBatchOptional(null);
    }
}