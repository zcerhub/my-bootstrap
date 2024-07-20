package com.dap.sequence.server.controller.v1;

import com.dap.sequence.api.core.SeqServerProducer;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.dto.SeqSdkMonitorDto;
import com.dap.sequence.api.dto.SeqTransferDto;
import com.dap.sequence.api.dto.SnowflakeRcvCluster;
import com.dap.sequence.api.exception.ResultResponse;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.server.core.AsyncTask;
import com.dap.sequence.server.entity.DataSourceInfoEntity;
import com.dap.sequence.server.entity.SeqDesignPo;
import com.dap.sequence.server.entity.SeqSdkMonitor;
import com.dap.sequence.server.entity.SeqServiceNode;
import com.dap.sequence.server.factory.AbstractSeqFlowEngine;
import com.dap.sequence.server.holder.SeqHolder;
import com.dap.sequence.server.service.SeqSdkMonitorService;
import com.dap.sequence.server.service.SeqServiceNodeService;
import com.dap.sequence.server.utils.EnvUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * @className SeqDesignControllerTest
 * @description TODO
 * @author renle
 * @date 2024/02/01 19:39:40 
 * @version: V23.06
 */
@RunWith(MockitoJUnitRunner.class)
public class SeqDesignControllerTest {

    @InjectMocks
    private SeqDesignController seqDesignController;

    @Spy
    private SeqServiceNodeService seqServiceNodeService;

    @Spy
    private SeqServerProducer seqServerProducer;

    @Spy
    private AsyncTask asyncTask;

    @Spy
    private SeqSdkMonitorService seqSdkMonitorService;

    @Test
    public void getStringIps() {
        SeqServiceNode seqServiceNode = new SeqServiceNode() {
            {
                setClusterId("123");
            }
        };
        when(seqServiceNodeService.getObjectByMap(Mockito.anyMap())).thenReturn(seqServiceNode);
        List<SeqServiceNode> listNode = new ArrayList<SeqServiceNode>() {
            {
                add(new SeqServiceNode() {
                    {
                        setHostIp("127.0.0.1");
                        setPort(8080);
                    }
                });
            }
        };
        when(seqServiceNodeService.queryList(Mockito.anyMap())).thenReturn(listNode);
        ResultResponse response = seqDesignController.getStringIps(null, null);
        Assert.assertEquals("127.0.0.1:8080", response.getData().toString());
    }

    @Test
    public void getStringSeq() {
        SeqDesignController designController = Mockito.spy(seqDesignController);
        SeqTransferDto seqTransferDto = new SeqTransferDto() {
            {
                setList(new ArrayList<Object>() {
                    {
                        add("123");
                    }
                });
            }
        };
        when(seqServerProducer.getSeqFormServer(Mockito.any())).thenReturn(seqTransferDto);
        doNothing().when(asyncTask).saveUseInfo(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        SeqDesignPo seqDesignPo = new SeqDesignPo();
        doReturn(seqDesignPo).when(designController).sequenceCodeValidation(Mockito.any());
        //when(seqDesignController.sequenceCodeValidation(Mockito.any())).thenReturn(seqDesignPo);
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqCode("123");
                setTenantId("123");
                setDay("123");
            }
        };
        HttpEntity<SeqParamsDto> request = new HttpEntity<>(seqParamsDto);
        ResultResponse resultResponse = designController.getStringSeq(request, null);
        SeqTransferDto dto = (SeqTransferDto) resultResponse.getData();
        Assert.assertEquals(1, dto.getList().size());
    }

    @Test
    public void getIncreaseSeq() {
        SeqDesignController designController = Mockito.spy(seqDesignController);
        SeqTransferDto seqTransferDto = new SeqTransferDto() {
            {
                setList(new ArrayList<Object>() {
                    {
                        add("123");
                    }
                });
            }
        };
        when(seqServerProducer.createIncreaseSeq(Mockito.any())).thenReturn(seqTransferDto);
        doNothing().when(asyncTask).saveUseInfo(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        SeqDesignPo seqDesignPo = new SeqDesignPo();
        doReturn(seqDesignPo).when(designController).sequenceCodeValidation(Mockito.any());
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqCode("123");
                setTenantId("123");
                setDay("123");
            }
        };
        HttpEntity<SeqParamsDto> request = new HttpEntity<>(seqParamsDto);
        ResultResponse resultResponse = designController.getIncreaseSeq(request, null);
        SeqTransferDto dto = (SeqTransferDto) resultResponse.getData();
        Assert.assertEquals(1, dto.getList().size());
    }

    @Test
    public void getRecoverySeq() {
        SeqDesignController designController = Mockito.spy(seqDesignController);
        SeqTransferDto seqTransferDto = new SeqTransferDto() {
            {
                setList(new ArrayList<Object>() {
                    {
                        add("123");
                    }
                });
            }
        };
        when(seqServerProducer.createRecoverySeq(Mockito.any())).thenReturn(seqTransferDto);
        doNothing().when(asyncTask).saveUseInfo(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        SeqDesignPo seqDesignPo = new SeqDesignPo();
        doReturn(seqDesignPo).when(designController).sequenceCodeValidation(Mockito.any());
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqCode("123");
                setTenantId("123");
                setDay("123");
            }
        };
        HttpEntity<SeqParamsDto> request = new HttpEntity<>(seqParamsDto);
        ResultResponse resultResponse = designController.getRecoverySeq(request, null);
        SeqTransferDto dto = (SeqTransferDto) resultResponse.getData();
        Assert.assertEquals(1, dto.getList().size());
    }

    @Test
    public void recoverySeq() {
        SeqDesignController designController = Mockito.spy(seqDesignController);
        SeqTransferDto seqTransferDto = new SeqTransferDto() {
            {
                setList(new ArrayList<Object>() {
                    {
                        add("123");
                    }
                });
            }
        };
        when(seqServerProducer.recoverySeq(Mockito.any())).thenReturn(seqTransferDto);
        SeqDesignPo seqDesignPo = new SeqDesignPo();
        doReturn(seqDesignPo).when(designController).sequenceCodeValidation(Mockito.any());
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqCode("123");
                setTenantId("123");
                setDay("123");
            }
        };
        HttpEntity<SeqParamsDto> request = new HttpEntity<>(seqParamsDto);
        ResultResponse resultResponse = designController.recoverySeq(request, null);
        SeqTransferDto dto = (SeqTransferDto) resultResponse.getData();
        Assert.assertEquals(1, dto.getList().size());
    }

    @Test
    public void getOptionalSeq() {
        SeqDesignController designController = Mockito.spy(seqDesignController);
        SeqTransferDto seqTransferDto = new SeqTransferDto() {
            {
                setList(new ArrayList<Object>() {
                    {
                        add("123");
                    }
                });
            }
        };
        when(seqServerProducer.getAndCreateSeq(Mockito.any())).thenReturn(seqTransferDto);
        SeqDesignPo seqDesignPo = new SeqDesignPo();
        doReturn(seqDesignPo).when(designController).sequenceCodeValidation(Mockito.any());
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqCode("123");
                setTenantId("123");
                setDay("123");
            }
        };
        HttpEntity<SeqParamsDto> request = new HttpEntity<>(seqParamsDto);
        ResultResponse resultResponse = designController.getOptionalSeq(request, null);
        SeqTransferDto dto = (SeqTransferDto) resultResponse.getData();
        Assert.assertEquals(1, dto.getList().size());
    }

    @Test
    public void selectedOptionalSeq() {
        SeqDesignController designController = Mockito.spy(seqDesignController);
        doNothing().when(seqServerProducer).selectedOptionalSeq(Mockito.any());
        SeqDesignPo seqDesignPo = new SeqDesignPo();
        doReturn(seqDesignPo).when(designController).sequenceCodeValidation(Mockito.any());
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqCode("123");
                setTenantId("123");
                setDay("123");
                setSerialNumber("123");
            }
        };
        HttpEntity<SeqParamsDto> request = new HttpEntity<>(seqParamsDto);
        ResultResponse resultResponse = designController.selectedOptionalSeq(request, null);
        Assert.assertEquals("S2001", resultResponse.getCode());
    }

    @Test
    public void cancelOptionalSeq() {
        SeqDesignController designController = Mockito.spy(seqDesignController);
        doNothing().when(seqServerProducer).cancelOptionalSeq(Mockito.any());
        SeqDesignPo seqDesignPo = new SeqDesignPo();
        doReturn(seqDesignPo).when(designController).sequenceCodeValidation(Mockito.any());
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqCode("123");
                setTenantId("123");
                setDay("123");
                setSerialNumber("123");
            }
        };
        HttpEntity<SeqParamsDto> request = new HttpEntity<>(seqParamsDto);
        ResultResponse resultResponse = designController.cancelOptionalSeq(request, null);
        Assert.assertEquals("S2001", resultResponse.getCode());
    }

    @Test(expected = SequenceException.class)
    public void cancelOptionalSeq_error() {
        SeqDesignController designController = Mockito.spy(seqDesignController);
        SeqDesignPo seqDesignPo = new SeqDesignPo();
        doReturn(seqDesignPo).when(designController).sequenceCodeValidation(Mockito.any());
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqCode("123");
                setTenantId("123");
                setDay("123");
            }
        };
        HttpEntity<SeqParamsDto> request = new HttpEntity<>(seqParamsDto);
        ResultResponse resultResponse = designController.cancelOptionalSeq(request, null);
    }

    @Test
    public void saveSdkMonitor() {
        SeqServiceNode seqServiceNode = new SeqServiceNode() {
            {
                setClusterId("123");
                setTenantId("123");
            }
        };
        when(seqServiceNodeService.getObjectByMap(Mockito.anyMap())).thenReturn(seqServiceNode);
        try (MockedStatic<EnvUtils> mockedStatic = Mockito.mockStatic(EnvUtils.class)) {
            mockedStatic.when(() -> EnvUtils.getIpAddress(Mockito.any())).thenReturn("127.0.0.1");
            SeqSdkMonitorDto seqSdkMonitorDto = new SeqSdkMonitorDto();
            HttpEntity<SeqSdkMonitorDto> request = new HttpEntity<>(seqSdkMonitorDto);
            ResultResponse resultResponse = seqDesignController.saveSdkMonitor(request, null);
            Assert.assertEquals("S2001", resultResponse.getCode());
        }
    }

    @Test
    public void intWorkId() {
        SeqServiceNode seqServiceNode = new SeqServiceNode() {
            {
                setClusterId("123");
                setTenantId("123");
            }
        };
        when(seqServiceNodeService.getObjectByMap(Mockito.anyMap())).thenReturn(seqServiceNode);
        SeqSdkMonitor seqSdkMonitor = new SeqSdkMonitor();
        seqSdkMonitor.setWorkId(123456);
        when(seqSdkMonitorService.initWorkId(Mockito.any())).thenReturn(seqSdkMonitor);
        SeqSdkMonitorDto seqSdkMonitorDto = new SeqSdkMonitorDto() {
            {
                setInstanceName("123");
            }
        };
        HttpEntity<SeqSdkMonitorDto> request = new HttpEntity<>(seqSdkMonitorDto);
        ResultResponse resultResponse = seqDesignController.intWorkId(request);
        Assert.assertEquals("123456", resultResponse.getData().toString());
    }

    @Test
    public void liveWorkId() {
        when(seqSdkMonitorService.liveWorkId(Mockito.any())).thenReturn(true);
        SeqSdkMonitorDto seqSdkMonitorDto = new SeqSdkMonitorDto() {
            {
                setInstanceName("123");
            }
        };
        HttpEntity<SeqSdkMonitorDto> request = new HttpEntity<>(seqSdkMonitorDto);
        ResultResponse resultResponse = seqDesignController.liveWorkId(request);
        Assert.assertTrue(Boolean.parseBoolean(resultResponse.getData().toString()));
    }


    @Test
    public void rcvWorIdByClient() {
        when(seqSdkMonitorService.rcvWorIdByClient(Mockito.any())).thenReturn(1);
        SnowflakeRcvCluster snowflakeRcvCluster = new SnowflakeRcvCluster() {
            {
                setInstanceName("123");
                setWorkId(1);
            }
        };
        HttpEntity<SnowflakeRcvCluster> request = new HttpEntity<>(snowflakeRcvCluster);
        ResultResponse resultResponse = seqDesignController.rcvWorIdByClient(request);
        Assert.assertEquals("1", resultResponse.getData().toString());
    }

    @Test
    public void getSeqObj() {
        AbstractSeqFlowEngine seqFlowEngine = new AbstractSeqFlowEngine() {
            @Override
            public void loadConf() {
            }

            @Override
            public void loadConf(SeqDesignPo seqDesignPo) {
            }
        };
        seqFlowEngine.setSeqObj(new SeqObj() {
            {
                setSequenceCode("123");
            }
        });
        SeqHolder.getEngineMap().put("123@123", seqFlowEngine);
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqCode("123");
                setDay("123");
                setTenantId("123");
            }
        };
        HttpEntity<SeqParamsDto> request = new HttpEntity<>(seqParamsDto);
        ResultResponse resultResponse = seqDesignController.getSeqObj(request, null);
        SeqObj obj = (SeqObj) resultResponse.getData();
        Assert.assertEquals("123", obj.getSequenceCode());
    }

    @Test
    public void cutDataSource() {
        DataSourceInfoEntity sourceInfoEntity = new DataSourceInfoEntity() {
            {
                setDatasourceName("123");
                setDatasourceUrl("123");
                setDatasourcePassword("123");
                setDriverClassName("123");
                setDatasourceName("123");
            }
        };
        ResultResponse resultResponse = seqDesignController.cutDataSource(sourceInfoEntity);
        Assert.assertEquals("S2001", resultResponse.getCode());
    }

    @Test
    public void sequenceCodeValidation() {
        AbstractSeqFlowEngine seqFlowEngine = new AbstractSeqFlowEngine() {
            @Override
            public void loadConf() {
            }

            @Override
            public void loadConf(SeqDesignPo seqDesignPo) {
            }
        };
        seqFlowEngine.setSeqObj(new SeqObj() {
            {
                setSequenceCode("123");
            }
        });
        SeqHolder.getEngineMap().put("123@123", seqFlowEngine);
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setSeqCode("123");
                setDay("123");
                setTenantId("123");
            }
        };
        SeqDesignPo seqDesignPo = seqDesignController.sequenceCodeValidation(seqParamsDto);
        Assert.assertEquals("123", seqDesignPo.getSequenceCode());
    }
}