package com.dap.sequence.server.service.impl;

import com.dap.sequence.api.core.BaseRuleHandler;
import com.dap.sequence.api.core.SeqProducer;
import com.dap.sequence.api.dto.CheckRuleInfo;
import com.dap.sequence.api.dto.Rule;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.dto.SeqTransferDto;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.server.core.AsyncTask;
import com.dap.sequence.server.entity.SeqDesignPo;
import com.dap.sequence.server.factory.AbstractSeqFlowEngine;
import com.dap.sequence.server.holder.SeqHolder;
import com.dap.sequence.server.rules.CheckRuleHandler;
import com.dap.sequence.server.service.SeqOptionalRecordService;
import com.dap.sequence.server.service.SeqRecoveryRecordService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * @className SeqServerProducerImplTest
 * @description TODO
 * @author renle
 * @date 2024/02/02 11:23:42 
 * @version: V23.06
 */
@RunWith(MockitoJUnitRunner.class)
public class SeqServerProducerImplTest {

    @InjectMocks
    private SeqServerProducerImpl seqServerProducer;

    @Spy
    private SeqProducer seqProducer;

    @Spy
    private SeqOptionalRecordService seqOptionalRecordService;

    @Spy
    private AsyncTask asyncTask;

    @Spy
    private SeqRecoveryRecordService seqRecoveryRecordService;

    private void buildSeqEngine() {
        AbstractSeqFlowEngine seqFlowEngine = new AbstractSeqFlowEngine() {
            @Override
            public void loadConf() {
            }

            @Override
            public void loadConf(SeqDesignPo seqDesignPo) {
            }
        };
        seqFlowEngine.setSeqProducer(new AtomicReference<SeqProducer>() {
            {
                set(seqProducer);
            }
        });
        seqFlowEngine.setSeqObj(new SeqObj() {
            {
                setId("123");
                setSequenceCode("123");
                setSequenceNumber(1);
                setRequestNumber(1);
            }
        });
        SeqHolder.getEngineMap().put("123@123", seqFlowEngine);
    }


    @Test(expected = SequenceException.class)
    public void getAndCreateSeq_error_01() {
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        seqServerProducer.getAndCreateSeq(seqParamsDto);
    }

    @Test(expected = SequenceException.class)
    public void getAndCreateSeq_error_02() {
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqVal("eewr");
            }
        };
        seqServerProducer.getAndCreateSeq(seqParamsDto);
    }

    @Test
    public void getAndCreateSeq_01() {
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqVal("123");
            }
        };
        SeqTransferDto seqTransferDto = seqServerProducer.getAndCreateSeq(seqParamsDto);
        Assert.assertNull(seqTransferDto.getList());
    }

    @Test
    public void getAndCreateSeq_02() {
        buildSeqEngine();
        List<Object> seqList = new ArrayList<Object>() {
            {
                add(1);
            }
        };
        when(seqProducer.createOptional(Mockito.any(), Mockito.any())).thenReturn(seqList);
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqVal("123");
                setSeqCode("123");
                setTenantId("123");
            }
        };
        SeqTransferDto seqTransferDto = seqServerProducer.getAndCreateSeq(seqParamsDto);
        Assert.assertEquals(1, seqTransferDto.getList().size());
    }

    @Test
    public void selectedOptionalSeq() {
        buildSeqEngine();
        List<Rule> rules = new ArrayList<Rule>() {
            {
                add(new Rule() {
                    {
                        setRuleType("7");
                        setRuleData(new CheckRuleInfo() {
                            {
                                setCheckLength(2);
                                setCheckPosition(0);
                            }
                        });
                    }
                });
            }
        };
        SeqHolder.getRulesMap().put("123", rules);
        BaseRuleHandler baseRuleHandler = Mockito.spy(CheckRuleHandler.class);
        try (MockedStatic<SeqHolder> holderMockedStatic = Mockito.mockStatic(SeqHolder.class)) {
            holderMockedStatic.when(() -> SeqHolder.getRuleHandlerByName(Mockito.any())).thenReturn(baseRuleHandler);
        }
        //when(baseRuleHandler.generateRule(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn("123");
        doNothing().when(seqOptionalRecordService).selectedOptionalRecord(Mockito.any());
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqVal("123");
                setSeqCode("123");
                setTenantId("123");
                setSerialNumber("12345");
            }
        };
        seqServerProducer.selectedOptionalSeq(seqParamsDto);
        Assert.assertEquals("123[2,0]", seqParamsDto.getSerialNumber());
    }

    @Test
    public void cancelOptionalSeq() {
        buildSeqEngine();
        List<Rule> rules = new ArrayList<Rule>() {
            {
                add(new Rule() {
                    {
                        setRuleType("7");
                        setRuleData(new CheckRuleInfo() {
                            {
                                setCheckLength(2);
                                setCheckPosition(0);
                            }
                        });
                    }
                });
            }
        };
        SeqHolder.getRulesMap().put("123", rules);
        BaseRuleHandler baseRuleHandler = Mockito.spy(CheckRuleHandler.class);
        try (MockedStatic<SeqHolder> holderMockedStatic = Mockito.mockStatic(SeqHolder.class)) {
            holderMockedStatic.when(() -> SeqHolder.getRuleHandlerByName(Mockito.any())).thenReturn(baseRuleHandler);
        }
        doNothing().when(seqOptionalRecordService).cancelOptionalRecord(Mockito.any());
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqVal("123");
                setSeqCode("123");
                setTenantId("123");
                setSerialNumber("12345");
            }
        };
        seqServerProducer.cancelOptionalSeq(seqParamsDto);
        Assert.assertEquals("123[2,0]", seqParamsDto.getSerialNumber());
    }

    @Test
    public void getSeqFormServer_error01() {
        SeqTransferDto seqTransferDto = seqServerProducer.getSeqFormServer(new SeqParamsDto());
        Assert.assertNull(seqTransferDto.getList());
    }

    @Test(expected = SequenceException.class)
    public void getSeqFormServer_error02() {
        AbstractSeqFlowEngine seqFlowEngine = new AbstractSeqFlowEngine() {
            @Override
            public void loadConf() {
            }

            @Override
            public void loadConf(SeqDesignPo seqDesignPo) {
            }
        };
        seqFlowEngine.setSeqProducer(new AtomicReference<SeqProducer>() {
            {
                set(seqProducer);
            }
        });
        seqFlowEngine.setSeqObj(new SeqObj() {
            {
                setId("123");
                setSequenceCode("1234");
                setSequenceNumber(0);
            }
        });
        SeqHolder.getEngineMap().put("1234@123", seqFlowEngine);
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqVal("1234");
                setSeqCode("1234");
                setTenantId("123");
                setSerialNumber("12345");
            }
        };
        SeqTransferDto seqTransferDto = seqServerProducer.getSeqFormServer(seqParamsDto);
    }

    @Test
    public void getSeqFormServer() {
        buildSeqEngine();
        BlockingQueue<Object> unUseQueue = new ArrayBlockingQueue<Object>(1) {
            {
                add(1);
            }
        };
        SeqHolder.putSequenceMap("123###123@123", unUseQueue);
        doNothing().when(asyncTask).asyncCreate(Mockito.any(), Mockito.any(), Mockito.any());
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqVal("123");
                setSeqCode("123");
                setDay("123");
                setTenantId("123");
                setSerialNumber("12345");
                setRequestNumber(0);
            }
        };
        SeqTransferDto seqTransferDto = seqServerProducer.getSeqFormServer(seqParamsDto);
        Assert.assertEquals(1, seqTransferDto.getList().size());
    }

    @Test
    public void createIncreaseSeq_error_01() {
        SeqTransferDto seqTransferDto = seqServerProducer.createIncreaseSeq(new SeqParamsDto());
        Assert.assertNull(seqTransferDto.getList());
    }

    @Test(expected = SequenceException.class)
    public void createIncreaseSeq_error02() {
        buildSeqEngine();
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqVal("123");
                setSeqCode("123");
                setDay("123");
                setTenantId("123");
                setSerialNumber("12345");
                setRequestNumber(0);
            }
        };
        SeqTransferDto seqTransferDto = seqServerProducer.createIncreaseSeq(seqParamsDto);
        Assert.assertEquals(1, seqTransferDto.getList().size());
    }

    @Test
    public void createIncreaseSeq() {
        AbstractSeqFlowEngine seqFlowEngine = new AbstractSeqFlowEngine() {
            @Override
            public void loadConf() {
            }

            @Override
            public void loadConf(SeqDesignPo seqDesignPo) {
            }
        };
        seqFlowEngine.setSeqProducer(new AtomicReference<SeqProducer>() {
            {
                set(seqProducer);
            }
        });
        seqFlowEngine.setSeqObj(new SeqObj() {
            {
                setId("123");
                setSequenceCode("123");
                setSequenceNumber(0);
                setRequestNumber(1);
            }
        });
        SeqHolder.getEngineMap().put("12345@12345", seqFlowEngine);
        List<Object> seqList = new ArrayList<Object>() {
            {
                add(1);
            }
        };
        when(seqProducer.createIncreaseSeq(Mockito.any(), Mockito.any())).thenReturn(seqList);
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqVal("123");
                setSeqCode("12345");
                setDay("123");
                setTenantId("12345");
                setSerialNumber("12345");
                setRequestNumber(0);
            }
        };
        SeqTransferDto seqTransferDto = seqServerProducer.createIncreaseSeq(seqParamsDto);
        Assert.assertEquals(1, seqTransferDto.getList().size());
    }

    @Test
    public void createRecoverySeq_error01() {
        SeqTransferDto seqTransferDto = seqServerProducer.createRecoverySeq(new SeqParamsDto());
        Assert.assertNull(seqTransferDto.getList());
    }

    @Test
    public void createRecoverySeq() {
        buildSeqEngine();
        List<Object> seqList = new ArrayList<Object>() {
            {
                add(1);
            }
        };
        when(seqProducer.createRecoverySeq(Mockito.any(), Mockito.any())).thenReturn(seqList);
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqVal("123");
                setSeqCode("123");
                setDay("123");
                setTenantId("123");
                setSerialNumber("123");
                setRequestNumber(1);
            }
        };
        SeqTransferDto seqTransferDto = seqServerProducer.createRecoverySeq(seqParamsDto);
        Assert.assertEquals(1, seqTransferDto.getList().size());
    }

    @Test
    public void recoverySeq() {
        doNothing().when(seqRecoveryRecordService).updateRecovery(Mockito.any());
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqVal("123");
                setSeqCode("123");
                setDay("123");
                setTenantId("123");
                setSerialNumber("123");
                setRequestNumber(1);
            }
        };
        SeqTransferDto seqTransferDto = seqServerProducer.recoverySeq(seqParamsDto);
        Assert.assertNotNull(seqTransferDto);
    }
}