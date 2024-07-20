package com.dap.sequence.server.controller.v1;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;
import com.dap.sequence.api.constant.SequenceConstant;
import com.dap.sequence.api.core.SeqServerProducer;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.dto.SeqTransferDto;
import com.dap.sequence.api.exception.ExceptionEnum;
import com.dap.sequence.api.exception.ResultResponse;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.server.config.DataSourceContextHolder;
import com.dap.sequence.server.config.DynamicDataSource;
import com.dap.sequence.server.entity.DataSourceInfoEntity;
import com.dap.sequence.server.entity.SeqDesignPo;
import com.dap.sequence.server.entity.SeqInstanceRule;
import com.dap.sequence.server.entity.SeqServiceNode;
import com.dap.sequence.server.service.SeqDesignService;
import com.dap.sequence.server.service.SeqPlatformService;
import com.dap.sequence.server.service.SeqSdkMonitorService;
import com.dap.sequence.server.service.SeqServiceNodeService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: cao
 * Data: 2022/2/28 16:31
 * @Descricption:
 */
@RestController
@RequestMapping("/seq/dynamic")
public class DemoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    private SeqDesignService seqDesignService;

    @Autowired
    private SeqServerProducer seqServerProducer;

    @Autowired
    private SeqServiceNodeService seqServiceNodeService;

    @Autowired
    private SeqPlatformService seqFromPlatformService;

    @GetMapping("/ceshi/{id}")
    public void ceshi(@PathVariable("id") String id) {
        //boolean seqDesignPos = seqFromPlatformService.isUsePlatform();
        //List<SeqDesignPo> seqDesignPos = seqFromPlatformService.getAllSeqDesign();
        List<SeqInstanceRule> seqDesignPos = seqFromPlatformService.getSeqInstanceRules(null);
        //SeqDesignPo designPo = seqDesignService.getObjectById(id);
        LOGGER.info("ceshi designPo = {}", JSONObject.toJSONString(seqDesignPos));
    }


    @GetMapping("/getTest/{id}")
    public String getTest(@PathVariable("id") String id) {
        DataSourceInfoEntity sourceInfoEntity = new DataSourceInfoEntity();
        sourceInfoEntity.setDatasourceName("dynamic-slave");
        sourceInfoEntity.setDriverClassName("com.mysql.cj.jdbc.Driver");
        sourceInfoEntity.setDatasourceUrl("jdbc:mysql://10.114.14.65:3306/paas_tenant_auth_test?autoReconnect=true&useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8");
        sourceInfoEntity.setDatasourceUsername("root");
        sourceInfoEntity.setDatasourcePassword("Esb#2021");

        SeqDesignPo designPo = seqDesignService.getObjectById(id);
        LOGGER.info("getTest designPo = {}", JSONObject.toJSONString(designPo));
        setDataSourceInfo(sourceInfoEntity);

        SeqDesignPo designPo11 = seqDesignService.getObjectById(id);
        LOGGER.info("getTest designPo11 = {}", JSONObject.toJSONString(designPo11));
        return "";
    }


    /**
     * 根据集群 切换数据源
     */
    private void setDataSourceInfo(DataSourceInfoEntity sourceInfoEntity) {
        List<DataSourceInfoEntity> list = new ArrayList<>();
        //查询数据库得到
        list.add(sourceInfoEntity);
        //使用自己设置的sourceName作为数据源的key，使用这个进行切换
        Map<Object, Object> dataSourceMap = list.stream().collect(Collectors.toMap(DataSourceInfoEntity::getDatasourceName, x -> {
            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setDriverClassName(x.getDriverClassName());
            druidDataSource.setUrl(x.getDatasourceUrl());
            druidDataSource.setUsername(x.getDatasourceUsername());
            druidDataSource.setPassword(x.getDatasourcePassword());
            druidDataSource.setValidationQuery("SELECT 1 FROM DUAL");
            druidDataSource.setKeepAlive(true);
            return druidDataSource;
        }));
        DynamicDataSource dynamicDataSource = DynamicDataSource.getInstance();
        Map<Object, Object> map = dynamicDataSource.getDataSourceMap();
        map.putAll(dataSourceMap);
        dynamicDataSource.setTargetDataSources(map);
        dynamicDataSource.afterPropertiesSet();
        DataSourceContextHolder.setDBType(sourceInfoEntity.getDatasourceName());
    }

    @GetMapping("/getStringSeq/{seqCode}")
    public ResultResponse getStringSeq(@RequestHeader(value = "tenantId") String tenantId, @PathVariable("seqCode") String seqCode) {
        SeqParamsDto seqParamsDto = new SeqParamsDto(seqCode, "base");
        seqParamsDto.setTenantId(tenantId);
        SeqTransferDto seqFormServer;
        checkSeqCode(seqCode);
        seqFormServer = seqServerProducer.getSeqFormServer(seqParamsDto);
        Optional.ofNullable(seqFormServer)
                .map(SeqTransferDto::getList)
                .filter(objs -> !objs.isEmpty())
                .orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_FROM_SERVER_EMPTY));
        return ResultResponse.success(seqFormServer);
    }


    @GetMapping("/getStringIps/{seqName}")
    public ResultResponse getStringIps(@PathVariable("seqName") String seqName) {
        SeqServiceNode seqServiceNode = seqServiceNodeService.getObjectByClusterName(seqName);
        String result = Optional.ofNullable(seqServiceNode)
                .map(serviceNode -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("name", seqName);
                    StringBuilder str = new StringBuilder();
                    List<SeqServiceNode> listNode = seqServiceNodeService.queryList(map);
                    listNode.forEach(node -> str.append(node.getHostIp()).append(":").append(node.getPort()).append(","));
                    return str.deleteCharAt(str.length() - 1).toString();
                })
                .orElseGet(() -> seqName + "找不到对应的集群");
        return ResultResponse.success(result);
    }


    /**
     * 检查序列是否存在，进行校验
     *
     * @param seqCode seqCode
     */
    private void checkSeqCode(String seqCode) {
        SeqDesignPo seqDesignPo = new SeqDesignPo();
        String code = StringUtils.substringBefore(seqCode, SequenceConstant.DAY_SWITCH_SPLIT);
        seqDesignPo.setSequenceCode(code);
        List<SeqDesignPo> designPos = seqDesignService.queryList(seqDesignPo);
        Optional.ofNullable(designPos)
                .filter(seqDesignPos -> !seqDesignPos.isEmpty())
                .orElseThrow(() -> {
                    LOGGER.error("seqCode {} not found", seqCode);
                    return new SequenceException(ExceptionEnum.SEQ_NOT_FOUND);
                });
    }


}
