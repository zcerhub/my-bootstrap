package com.dap.sequence.server.core;

import com.dap.sequence.api.core.BaseRuleHandler;
import com.dap.sequence.api.dto.DateRuleInfo;
import com.dap.sequence.api.dto.EnumRuleInfo;
import com.dap.sequence.api.dto.NumberRuleInfo;
import com.dap.sequence.api.dto.Rule;
import com.dap.sequence.api.dto.SeqCallbackDto;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.server.holder.SeqHolder;
import com.dap.sequence.server.service.SeqOptionalRecordService;
import com.dap.sequence.server.service.SeqRecoveryRecordService;
import com.dap.sequence.server.service.impl.SeqNumService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.util.ReflectionUtils.setField;

/**
 * @className SerialSeqProducerTest
 * @description TODO
 * @author renle
 * @date 2024/02/02 15:11:11 
 * @version: V23.06
 */
@RunWith(MockitoJUnitRunner.class)
public class SerialSeqProducerTest {

    //@InjectMocks
    private static SerialSeqProducer seqProducer = Mockito.mock(SerialSeqProducer.class);

    //@Spy
    private static SeqNumService seqNumService = Mockito.mock(SeqNumService.class);

    //@Spy
    private static SeqOptionalRecordService seqOptionalRecordService = Mockito.mock(SeqOptionalRecordService.class);

    //@Spy
    private static SeqRecoveryRecordService seqRecoveryRecordService = Mockito.mock(SeqRecoveryRecordService.class);

    @BeforeClass
    public static void before() {
        setField(getField(seqProducer, "seqNumService"), seqProducer, seqNumService);
        setField(getField(seqProducer, "seqOptionalRecordService"), seqProducer, seqOptionalRecordService);
        setField(getField(seqProducer, "seqRecoveryRecordService"), seqProducer, seqRecoveryRecordService);
    }

    private static Field getField(SerialSeqProducer seqProducer, String fieldName) {
        try {
            Field field = seqProducer.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void loadSeq() {
        SeqCallbackDto seqCallbackDto = new SeqCallbackDto();
        List callBackNumList = new ArrayList();
        seqProducer.loadSeq(seqCallbackDto, callBackNumList);
        Assert.assertNotNull(seqCallbackDto);
    }

    @Test
    public void createSeqAndCache_01() {
        seqProducer.createSeqAndCache(null, null);
        Mockito.verify(seqProducer, Mockito.times(1)).createSeqAndCache(Mockito.any(), Mockito.any());
    }

    @Test
    public void createSeqAndCache_02() {
        doNothing().when(seqNumService).addCallbackSeq(Mockito.any(), Mockito.any(), Mockito.any());
        SerialSeqProducer serialSeqProducer = Mockito.spy(seqProducer);
        List<Object> seqList = new ArrayList<Object>() {
            {
                add(1);
            }
        };
        doReturn(seqList).when(serialSeqProducer).createSeq(Mockito.any(), Mockito.any());
        SeqObj seqObj = new SeqObj() {
            {
                setSequenceNumber(1);
                setRequestNumber(1);
                setServerRecoverySwitch("1");
            }
        };
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqVal("123");
                setSeqCode("123");
                setDay("12345");
                setTenantId("123");
                setSerialNumber("123");
                setAsync(true);
            }
        };
        serialSeqProducer.createSeqAndCache(seqObj, seqParamsDto);
        int size = SeqHolder.getSequenceMapBySequenceCode(seqParamsDto.seqCacheKey()).size();
        Assert.assertEquals(1, size);
    }

    @Test(expected = Exception.class)
    public void createSeq_error01() {
        SerialSeqProducer serialSeqProducer = Mockito.spy(seqProducer);
        SeqObj seqObj = new SeqObj() {
            {
                setId("1");
            }
        };
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqCode("123");
                setDay("123");
                setTenantId("123");
                setSerialNumber("123");
            }
        };
        serialSeqProducer.createSeq(seqObj, seqParamsDto);
    }

    @Test
    public void createSeq_01() {
        BaseRuleHandler baseRuleHandler = Mockito.spy(BaseRuleHandler.class);
        try (MockedStatic<SeqHolder> holderMockedStatic = Mockito.mockStatic(SeqHolder.class)) {
            holderMockedStatic.when(() -> SeqHolder.getRuleHandlerByName(Mockito.any())).thenReturn(baseRuleHandler);
            //when(baseRuleHandler.generateRule(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(123);
        }
        LinkedList<Long> linkedList = new LinkedList<Long>() {
            {
                add(1L);
            }
        };
        when(seqNumService.updateNum(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(linkedList);
        doNothing().when(seqNumService).updateEnum(Mockito.any(), Mockito.any());
        SerialSeqProducer serialSeqProducer = Mockito.spy(seqProducer);
        List<Rule> rules = new ArrayList<Rule>() {
            {
                add(new Rule() {
                    {
                        setRuleType("3");
                        setRuleData(new DateRuleInfo() {
                            {
                                setDateFormat("yyyyMMdd");
                            }
                        });
                    }
                });
                add(new Rule() {
                    {
                        setRuleType("4");
                        setRuleData(new NumberRuleInfo());
                    }
                });
                add(new Rule() {
                    {
                        setRuleType("2");
                        setRuleData(new EnumRuleInfo() {
                            {
                                setIndex(1);
                                setEnumStore(new ArrayList<String>() {
                                    {
                                        add("1");
                                        add("2");
                                        add("3");
                                    }
                                });
                            }
                        });
                    }
                });
            }
        };
        SeqHolder.getRulesMap().put("1", rules);
        SeqObj seqObj = new SeqObj() {
            {
                setId("1");
            }
        };
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqCode("123");
                setDay("123");
                setTenantId("123");
                setSerialNumber("123");
            }
        };
        List<Object> seqList = serialSeqProducer.createSeq(seqObj, seqParamsDto);
        Assert.assertEquals(1, seqList.size());
    }

    @Test(expected = SequenceException.class)
    public void createIncreaseSeq_error01() {
        SerialSeqProducer serialSeqProducer = Mockito.spy(seqProducer);
        serialSeqProducer.createIncreaseSeq(new SeqObj() {
            {
                setId("wewe");
            }
        }, new SeqParamsDto());
    }

    @Test
    public void createIncreaseSeq() {
        BaseRuleHandler baseRuleHandler = Mockito.spy(BaseRuleHandler.class);
        try (MockedStatic<SeqHolder> holderMockedStatic = Mockito.mockStatic(SeqHolder.class)) {
            holderMockedStatic.when(() -> SeqHolder.getRuleHandlerByName(Mockito.any())).thenReturn(baseRuleHandler);
        }
        LinkedList<Long> linkedList = new LinkedList<Long>() {
            {
                add(1L);
            }
        };
        when(seqNumService.updateNum(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(linkedList);
        SerialSeqProducer serialSeqProducer = Mockito.spy(seqProducer);
        List<Rule> rules = new ArrayList<Rule>() {
            {
                add(new Rule() {
                    {
                        setRuleType("4");
                        setRuleData(new NumberRuleInfo());
                    }
                });
            }
        };
        SeqHolder.getRulesMap().put("1", rules);
        SeqObj seqObj = new SeqObj() {
            {
                setId("1");
            }
        };
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqCode("123");
                setDay("123");
                setTenantId("123");
                setSerialNumber("123");
            }
        };
        List<Object> seqList = serialSeqProducer.createIncreaseSeq(seqObj, seqParamsDto);
        Assert.assertEquals(1, seqList.size());
    }

    @Test
    public void createRecoverySeq() {
        when(seqRecoveryRecordService.selectRecoveryForUpdate(Mockito.any(), Mockito.any())).thenReturn(new ArrayList<>());
        BaseRuleHandler baseRuleHandler = Mockito.spy(BaseRuleHandler.class);
        try (MockedStatic<SeqHolder> holderMockedStatic = Mockito.mockStatic(SeqHolder.class)) {
            holderMockedStatic.when(() -> SeqHolder.getRuleHandlerByName(Mockito.any())).thenReturn(baseRuleHandler);
        }
        LinkedList<Long> linkedList = new LinkedList<Long>() {
            {
                add(1L);
            }
        };
        when(seqNumService.updateNum(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(linkedList);
        when(seqRecoveryRecordService.saveRecoveryRecord(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(1);
        SerialSeqProducer serialSeqProducer = Mockito.spy(seqProducer);
        List<Rule> rules = new ArrayList<Rule>() {
            {
                add(new Rule() {
                    {
                        setRuleType("4");
                        setRuleData(new NumberRuleInfo());
                    }
                });
            }
        };
        SeqHolder.getRulesMap().put("1", rules);
        SeqObj seqObj = new SeqObj() {
            {
                setId("1");
            }
        };
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqCode("123");
                setDay("123");
                setTenantId("123");
                setSerialNumber("123");
            }
        };
        List<Object> seqList = serialSeqProducer.createRecoverySeq(seqObj, seqParamsDto);
        Assert.assertEquals(1, seqList.size());
    }

    @Test
    public void createOptional() {
        when(seqOptionalRecordService.queryRecoveryOptional(Mockito.any(), Mockito.any())).thenReturn(new LinkedList<>());
        BaseRuleHandler baseRuleHandler = Mockito.spy(BaseRuleHandler.class);
        try (MockedStatic<SeqHolder> holderMockedStatic = Mockito.mockStatic(SeqHolder.class)) {
            holderMockedStatic.when(() -> SeqHolder.getRuleHandlerByName(Mockito.any())).thenReturn(baseRuleHandler);
        }
        LinkedList<Long> linkedList = new LinkedList<Long>() {
            {
                add(1L);
            }
        };
        when(seqNumService.updateNumOptional(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(linkedList);
        doNothing().when(seqNumService).saveBatchOptional(Mockito.any());
        SerialSeqProducer serialSeqProducer = Mockito.spy(seqProducer);
        List<Rule> rules = new ArrayList<Rule>() {
            {
                add(new Rule() {
                    {
                        setRuleType("4");
                        setRuleData(new NumberRuleInfo() {
                            {
                                setOptional(true);
                                setNumberEnd(10000L);
                            }
                        });
                        setRuleId("123");
                    }
                });
            }
        };
        SeqHolder.getRulesMap().put("1", rules);
        SeqObj seqObj = new SeqObj() {
            {
                setId("1");
            }
        };
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqCode("123");
                setDay("123");
                setTenantId("123");
                setSerialNumber("123");
                setSeqVal("888888");
                setCreateNumber(1);
            }
        };
        List<Object> seqList = serialSeqProducer.createOptional(seqObj, seqParamsDto);
        Assert.assertEquals(1, seqList.size());
    }
}