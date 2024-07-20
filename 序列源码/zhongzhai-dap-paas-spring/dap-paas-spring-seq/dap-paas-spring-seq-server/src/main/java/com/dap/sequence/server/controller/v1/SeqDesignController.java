package com.dap.sequence.server.controller.v1;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.base.api.entity.BaseEntity;
import com.base.api.exception.ServiceException;
import com.dap.sequence.api.commons.ResultEnum;
import com.dap.sequence.api.core.SeqServerProducer;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.dto.SeqSdkMonitorDto;
import com.dap.sequence.api.dto.SeqTransferDto;
import com.dap.sequence.api.dto.SnowflakeRcvCluster;
import com.dap.sequence.api.exception.ExceptionEnum;
import com.dap.sequence.api.exception.ResultResponse;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.api.util.SeqKeyUtils;
import com.dap.sequence.server.config.DataSourceContextHolder;
import com.dap.sequence.server.config.DynamicDataSource;
import com.dap.sequence.server.core.AsyncTask;
import com.dap.sequence.server.dao.SeqSdkMonitorDao;
import com.dap.sequence.server.entity.*;
import com.dap.sequence.server.factory.AbstractSeqFlowEngine;
import com.dap.sequence.server.factory.SeqFlowEngineFactory;
import com.dap.sequence.server.holder.SeqHolder;
import com.dap.sequence.server.service.SeqDesignService;
import com.dap.sequence.server.service.SeqSdkMonitorService;
import com.dap.sequence.server.service.SeqServiceClusterService;
import com.dap.sequence.server.service.SeqServiceNodeService;
import com.dap.sequence.server.utils.ConstantUtil;
import com.dap.sequence.server.utils.EnvUtils;
import com.dap.sequence.server.utils.NetUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: liuz
 * Data: 2022/1/19 14:13
 * @Descricption:
 */
@RestController
@RequestMapping("/v1/seq/publish")
public class SeqDesignController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeqDesignController.class);

    private static final String NORMAL_STATUS = "1";

    private static final String HOST_IP = "hostIp";

    private static final String POST = "port";

    /**
     * 未获取CLUSTER_NAME信息，则给的是"ALL"
     */
    private static final String DEFAULT_CLUSTER_ALL = "ALL";

    @Autowired
    private SeqServerProducer seqServerProducer;

    @Autowired
    private AsyncTask asyncTask;

    @Autowired
    private SeqSdkMonitorService seqSdkMonitorService;

    @Autowired
    private SeqServiceNodeService seqServiceNodeService;

    @Autowired
    private SeqDesignService seqDesignService;

    @Value("${server.port}")
    private int port;

    @Value("${gientech.dap.sequence.serviceNodeIp:}")
    private String serviceNodeIp;


    /**
     * 获取ip地址
     *
     * @param request request
     * @param httpServletRequest httpServletRequest
     * @return Result
     */
    @PostMapping("/getStringIps")
    public ResultResponse getStringIps(HttpEntity<String> request, HttpServletRequest httpServletRequest) {
        // 获取序列服务端ip
        Map<String, Object> ipPortMap = getIpPortMap();
        SeqServiceNode seqServiceNode = seqServiceNodeService.getObjectByMap(ipPortMap);
        Optional.ofNullable(seqServiceNode)
                .orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_CLUSTER_NOT_FOUND));

        Map<String, String> map = new HashMap<>();
        map.put("clusterId", seqServiceNode.getClusterId());
        List<SeqServiceNode> listNode = seqServiceNodeService.queryList(map);
        Optional.ofNullable(listNode)
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_NODE_NOT_FOUND));
        LOGGER.info("=====getStringIps=====服务端ip信息：{}", JSON.toJSON(listNode));
        StringBuilder str = new StringBuilder();
        listNode.forEach(node -> str.append(node.getHostIp()).append(":").append(node.getPort()).append(","));
        return ResultResponse.success(str.deleteCharAt(str.length() - 1));
    }

    private Map<String, Object> getIpPortMap() {
        return Optional.ofNullable(serviceNodeIp)
                .filter(StringUtils::isNotBlank)
                .map(nodeIp -> {
                    Map<String, Object> map = new HashMap<>();
                    String[] serviceNodeIpArr = serviceNodeIp.split(":");
                    map.put(HOST_IP, serviceNodeIpArr[0]);
                    map.put(POST, serviceNodeIpArr[1]);
                    return map;
                }).orElseGet(() -> {
                    Map<String, Object> map = new HashMap<>();
                    String ip = NetUtils.ip();
                    map.put(HOST_IP, ip);
                    map.put(POST, port);
                    return map;
                });
    }

    /**
     * 发布rest服务接口
     *
     * @param request request
     * @param httpServletRequest httpServletRequest
     * @return Result
     */
    @PostMapping("/getStringSeq")
    public ResultResponse getStringSeq(HttpEntity<SeqParamsDto> request, HttpServletRequest httpServletRequest) {
        SeqParamsDto seqParamsDto = request.getBody();
        SeqDesignPo seqDesignPo = sequenceCodeValidation(seqParamsDto);
        SeqTransferDto seqTransferDto = seqServerProducer.getSeqFormServer(seqParamsDto);
        List<Object> list = Optional.ofNullable(seqTransferDto)
                .map(SeqTransferDto::getList)
                .filter(objs -> !objs.isEmpty())
                .orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_FROM_SERVER_EMPTY));
        asyncTask.saveUseInfo(list, seqParamsDto, seqDesignPo, httpServletRequest);
        return ResultResponse.success(seqTransferDto);
    }

    /**
     * 发布rest服务接口
     *
     * @param request request
     * @param httpServletRequest httpServletRequest
     * @return Result
     */
    @PostMapping("/getIncreaseSeq")
    public ResultResponse getIncreaseSeq(HttpEntity<SeqParamsDto> request, HttpServletRequest httpServletRequest) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        SeqParamsDto seqParamsDto = request.getBody();
        SeqDesignPo seqDesignPo = sequenceCodeValidation(seqParamsDto);
        SeqTransferDto seqTransferDto = seqServerProducer.createIncreaseSeq(seqParamsDto);
        List<Object> list = Optional.ofNullable(seqTransferDto)
                .map(SeqTransferDto::getList)
                .filter(objs -> !objs.isEmpty())
                .orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_FROM_SERVER_EMPTY));
        asyncTask.saveUseInfo(list, seqParamsDto, seqDesignPo, httpServletRequest);
        stopWatch.stop();
        LOGGER.info("严格递增序列：{}, 创建耗时：{} ms", seqParamsDto.getSeqCode(), stopWatch.getTotalTimeMillis());
        return ResultResponse.success(seqTransferDto);
    }

    /**
     * 发布rest服务接口
     *
     * @param request request
     * @param httpServletRequest httpServletRequest
     * @return Result
     */
    @PostMapping("/getRecoverySeq")
    public ResultResponse getRecoverySeq(HttpEntity<SeqParamsDto> request, HttpServletRequest httpServletRequest) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        SeqParamsDto seqParamsDto = request.getBody();
        SeqDesignPo seqDesignPo = sequenceCodeValidation(seqParamsDto);
        SeqTransferDto seqTransferDto = seqServerProducer.createRecoverySeq(seqParamsDto);
        List<Object> list = Optional.ofNullable(seqTransferDto)
                .map(SeqTransferDto::getList)
                .filter(objs -> !objs.isEmpty())
                .orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_FROM_SERVER_EMPTY));
        asyncTask.saveUseInfo(list, seqParamsDto, seqDesignPo, httpServletRequest);
        stopWatch.stop();
        LOGGER.info("获取回收序列：{}, 创建耗时：{} ms", seqParamsDto.getSeqCode(), stopWatch.getTotalTimeMillis());
        return ResultResponse.success(seqTransferDto);
    }

    /**
     * 发布rest服务接口
     *
     * @param request request
     * @param httpServletRequest httpServletRequest
     * @return Result
     */
    @PostMapping("/recoverySeq")
    public ResultResponse recoverySeq(HttpEntity<SeqParamsDto> request, HttpServletRequest httpServletRequest) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        SeqParamsDto seqParamsDto = request.getBody();
        SeqDesignPo seqDesignPo = sequenceCodeValidation(seqParamsDto);
        SeqTransferDto seqTransferDto = seqServerProducer.recoverySeq(seqParamsDto);
        stopWatch.stop();
        LOGGER.info("回收序列：{}, 回收耗时：{} ms", seqDesignPo.getSequenceCode(), stopWatch.getTotalTimeMillis());
        return ResultResponse.success(seqTransferDto);
    }

    /**
     * 获取自选序列
     *
     * @param request request
     * @param httpServletRequest httpServletRequest
     * @return Result
     */
    @PostMapping("/getOptionalSeq")
    public ResultResponse getOptionalSeq(HttpEntity<SeqParamsDto> request, HttpServletRequest httpServletRequest) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        SeqParamsDto seqParamsDto = request.getBody();
        sequenceCodeValidation(seqParamsDto);
        SeqTransferDto seqTransferDto = seqServerProducer.getAndCreateSeq(seqParamsDto);
        stopWatch.stop();
        LOGGER.info("自选序列：{}, 组装耗时：{} ms", seqParamsDto.getSeqCode(), stopWatch.getTotalTimeMillis());
        return ResultResponse.success(seqTransferDto);
    }

    /**
     * 选定自选序列
     *
     * @param request request
     * @param httpServletRequest httpServletRequest
     * @return Result
     */
    @PostMapping("/selectedOptionalSeq")
    public ResultResponse selectedOptionalSeq(HttpEntity<SeqParamsDto> request, HttpServletRequest httpServletRequest) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        SeqParamsDto seqParamsDto = request.getBody();
        sequenceCodeValidation(seqParamsDto);
        checkOptionalParam(seqParamsDto);
        seqServerProducer.selectedOptionalSeq(seqParamsDto);
        stopWatch.stop();
        LOGGER.info("自选序列：{}, 选定耗时：{} ms", seqParamsDto.getSeqCode(), stopWatch.getTotalTimeMillis());
        return ResultResponse.success("选定成功");
    }

    /**
     * 取消自选
     *
     * @param request request
     * @param httpServletRequest httpServletRequest
     * @return Result
     */
    @PostMapping("/cancelOptionalSeq")
    public ResultResponse cancelOptionalSeq(HttpEntity<SeqParamsDto> request, HttpServletRequest httpServletRequest) {
        SeqParamsDto seqParamsDto = request.getBody();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        sequenceCodeValidation(seqParamsDto);
        checkOptionalParam(seqParamsDto);
        seqServerProducer.cancelOptionalSeq(seqParamsDto);
        stopWatch.stop();
        LOGGER.info("自选序列：{}, 取消耗时：{} ms", seqParamsDto.getSeqCode(), stopWatch.getTotalTimeMillis());
        return ResultResponse.success("取消成功");
    }

    private void checkOptionalParam(SeqParamsDto seqParamsDto) {
        if (StringUtils.isBlank(seqParamsDto.getSerialNumber())) {
            LOGGER.warn("序列编号 {} 自选序号为空!!! ", seqParamsDto.engineCacheKey());
            throw new SequenceException(ExceptionEnum.SEQ_OPTIONAL_SERIAL_EMPTY);
        }
    }

    /**
     * sdk 监控存储
     *
     * @param request request
     * @return Result
     */
    @PostMapping("/insert/sdk")
    public ResultResponse saveSdkMonitor(HttpEntity<SeqSdkMonitorDto> request, HttpServletRequest httpServletRequest) {
        SeqSdkMonitorDto dto = request.getBody();
        Optional.ofNullable(dto).orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_SDK_MONITOR_INFO_EMPTY));
        SeqSdkMonitor body = new SeqSdkMonitor();
        BeanUtils.copyProperties(dto, body);

        //获取序列服务端ip
        Map<String, Object> ipPortMap = getIpPortMap();
        SeqServiceNode seqServiceNode = seqServiceNodeService.getObjectByMap(ipPortMap);
        Optional.ofNullable(seqServiceNode).orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_NODE_NOT_FOUND));
        body.setTenantId(seqServiceNode.getTenantId());
        body.setServiceId(seqServiceNode.getClusterId());
        body.setStatus(NORMAL_STATUS);
        body.setHostIp(EnvUtils.getIpAddress(httpServletRequest));
        HashMap<String, String> map = new HashMap<>();
        map.put(HOST_IP, body.getHostIp());
        map.put(POST, body.getPort());
        List<SeqSdkMonitor> seqSdkMonitors = seqSdkMonitorService.queryList(map);
        LOGGER.info("=====sdk=====服务端ip信息：{}", JSON.toJSON(seqSdkMonitors));
        Integer integer = Optional.ofNullable(seqSdkMonitors)
                .filter(seqSdk -> !seqSdk.isEmpty())
                .map(seqSdk -> seqSdk.get(0))
                .map(BaseEntity::getId)
                .map(id -> {
                    body.setId(id);
                    return seqSdkMonitorService.updateObject(body);
                })
                .orElseGet(() -> seqSdkMonitorService.saveObject(body));
        LOGGER.info("sdk monitor insert or update num is {}", integer);
        return ResultResponse.success(ResultEnum.SUCCESS);
    }


    @Autowired
    private SeqSdkMonitorDao seqSdkMonitorDao;

    @Autowired
    private SeqServiceClusterService seqServiceClusterService;

    /**
     * 雪花算法-workId申请接口
     *
     * @param request 请求信息
     * @return
     */
    @PostMapping("/snowflakewordid")
    public ResultResponse intWorkId(HttpEntity<SeqSdkMonitorDto> request) {
        SeqSdkMonitorDto dto = request.getBody();
        if (dto == null) {
            throw new SequenceException(ExceptionEnum.SEQ_SNOW_INFO_EMPTY);
        }
        //验证instanceName
        if (StringUtils.isBlank(dto.getInstanceName())) {
            throw new SequenceException(ExceptionEnum.SEQ_SNOW_PARAM_ERROR);
        }


        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ConstantUtil.HOST_IP, dto.getHostIp());
        paramMap.put(ConstantUtil.PORT, dto.getPort());
        paramMap.put(ConstantUtil.STATUS_LIST, Arrays.asList(new String[] {ConstantUtil.MONITOR_STATUS_STOP,
                ConstantUtil.MONITOR_STATUS_RUNNING}));
//        获取正在运行、已经停止的客户端
        List<SeqSdkMonitor> seqSdkMonitors = seqSdkMonitorDao.querySeqSdkMonitorByHostIpAndPort(paramMap);
        if (ObjectUtil.isNotNull(seqSdkMonitors)) {
            for (SeqSdkMonitor seqSdkMonitor : seqSdkMonitors) {
//               只修改sdkName发生变动的SeqSdkMonitor状态为MONITOR_STATUS_DISCARDED
                if (StrUtil.equals(seqSdkMonitor.getSdkName(), dto.getSdkName())) {
                    continue;
                }
                Map<String, Object> seqSdkMonitorMap = new HashMap<>();
                seqSdkMonitorMap.put(ConstantUtil.SDK_NAME, seqSdkMonitor.getSdkName());
                seqSdkMonitorMap.put(ConstantUtil.INSTANCE_NAME, seqSdkMonitor.getInstanceName());
                seqSdkMonitorMap.put(ConstantUtil.UPDATE_DATE, new Date());
                seqSdkMonitorMap.put(ConstantUtil.STATUS, ConstantUtil.MONITOR_STATUS_DISCARDED);
                seqSdkMonitorDao.updateStatus(seqSdkMonitorMap);
            }
        }


        SeqSdkMonitor body = new SeqSdkMonitor();
//        body.setServiceName(dto.getServiceName());
        body.setHostIp(dto.getHostIp());
        body.setPort(dto.getPort());
        body.setInstanceName(dto.getInstanceName());
        body.setContextPath(dto.getContextPath());
        body.setSdkName(StringUtils.isBlank(dto.getSdkName()) ? DEFAULT_CLUSTER_ALL : dto.getSdkName());

        //获取序列服务端ip
        Map<String, Object> ipPortMap = getIpPortMap();
        SeqServiceNode seqServiceNode = seqServiceNodeService.getObjectByMap(ipPortMap);
        //获取序列服务信息
        if (seqServiceNode != null) {
            SeqServiceCluster seqServiceCluster = seqServiceClusterService.getObjectById(seqServiceNode.getClusterId());
            body.setTenantId(seqServiceNode.getTenantId());
            body.setServiceId(seqServiceNode.getClusterId());
            body.setServiceName(seqServiceCluster.getName());
        }
        LOGGER.info("starting request workId, client detail info is instanceName: {{}}, sdkName:{{}}", body.getInstanceName(), body.getSdkName());
        final SeqSdkMonitor seqSdkMonitor = seqSdkMonitorService.initWorkId(body);
        LOGGER.info("ended request workId, client detail info is instanceName: {{}}, workId:{{}}", body.getInstanceName(), seqSdkMonitor.getWorkId());
        return ResultResponse.success(seqSdkMonitor.getWorkId());
    }


    /**
     * 雪花算法-workId心跳接口接口
     *
     * @param request 请求信息
     * @return
     */
    @PostMapping("/heartbeat")
    public ResultResponse liveWorkId(HttpEntity<SeqSdkMonitorDto> request) {
        //1.拿到持久化的workId，需要更新或者新增
        SeqSdkMonitorDto dto = request.getBody();
        if (dto == null) {
            throw new SequenceException(ExceptionEnum.SEQ_SNOW_INFO_EMPTY);
        }
        //验证instanceName
        if (StringUtils.isBlank(dto.getInstanceName())) {
            throw new SequenceException(ExceptionEnum.SEQ_SNOW_PARAM_ERROR);
        }
        SeqSdkMonitor body = new SeqSdkMonitor();
        body.setSdkName(StringUtils.isBlank(dto.getSdkName()) ? DEFAULT_CLUSTER_ALL : dto.getSdkName());
        body.setInstanceName(dto.getInstanceName());
        body.setWorkId(dto.getWorkId());
        return ResultResponse.success(seqSdkMonitorService.liveWorkId(body));
    }

    /**
     * 自动回收
     *
     * @param request 请求信息
     * @return
     */
    @PostMapping("/rcvWorkIdAuto")
    public ResultResponse rcvWorkId(HttpEntity<SnowflakeRcvCluster> request) {
        SnowflakeRcvCluster rcvCluster = request.getBody();
        if (rcvCluster == null) {
            throw new SequenceException(ExceptionEnum.SEQ_SNOW_INFO_EMPTY);
        }
        return ResultResponse.success(seqSdkMonitorService.rcvSnowflakeWorkId(rcvCluster.getRcvWorkIdMaxIntervalTime()));
    }

    /**
     * 雪花算法-workId手动回收接口
     *
     * @param request 请求信息
     * @return
     */
    @PostMapping("/snowflake/recycle")
    public ResultResponse rcvWorIdByClient(HttpEntity<SnowflakeRcvCluster> request) {
        SnowflakeRcvCluster rcvCluster = request.getBody();
        if (rcvCluster == null) {
            throw new SequenceException(ExceptionEnum.SEQ_SDK_MONITOR_INFO_EMPTY);
        }

        //验证instanceName
        if (StringUtils.isBlank(rcvCluster.getInstanceName()) && Objects.isNull(rcvCluster.getWorkId())) {
            throw new SequenceException(ExceptionEnum.SEQ_SNOW_PARAM_ERROR);
        }

        SeqSdkMonitor sdkMonitor = new SeqSdkMonitor();
        sdkMonitor.setSdkName(StringUtils.isBlank(rcvCluster.getSdkName()) ? DEFAULT_CLUSTER_ALL : rcvCluster.getSdkName());
        sdkMonitor.setInstanceName(rcvCluster.getInstanceName());
        sdkMonitor.setWorkId(rcvCluster.getWorkId());
        return ResultResponse.success(seqSdkMonitorService.rcvWorIdByClient(sdkMonitor));
    }

    /**
     * 获取序列对象
     *
     * @param request request
     * @return Result
     */
    @PostMapping("/getSeqDesignObj")
    public ResultResponse getSeqObj(HttpEntity<SeqParamsDto> request, HttpServletRequest httpServletRequest) {
        SeqParamsDto seqParamsDto = request.getBody();
        Optional.ofNullable(seqParamsDto)
                .filter(seq -> StringUtils.isNotBlank(seq.engineCacheKey()))
                .orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_ENGINE_CACHE_KEY_NOT_FOUND));
        AbstractSeqFlowEngine seqFlowEngine = SeqHolder.getEngineMap().get(seqParamsDto.engineCacheKey());
        Optional.ofNullable(seqFlowEngine)
                .map(AbstractSeqFlowEngine::getSeqObj)
                .orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_NOT_FOUND));
        return ResultResponse.success(seqFlowEngine.getSeqObj());
    }


    /**
     * 下发后动态修改 缓存信息。
     *
     * @param seqDesignPo seqDesignPo
     * @return Result
     */
    @PostMapping("/updateSeqHolder")
    public ResultResponse updateSeqHolder(@RequestBody SeqDesignPo seqDesignPo) {
        try {
            SeqFlowEngineFactory.loadEngine(seqDesignPo);
            AbstractSeqFlowEngine seqFlowEngine = SeqHolder.getEngineMap().get(SeqKeyUtils.getSeqEngineKey(seqDesignPo.getSequenceCode(), seqDesignPo.getTenantId()));
            seqFlowEngine.refresh(seqDesignPo);
        } catch (ServiceException e) {
            LOGGER.error("updateSeqHolder 更新失败", e);
            return ResultResponse.error(ExceptionEnum.SEQ_UPDATE_HOLDER_FAILED);
        }
        return ResultResponse.success(ResultEnum.SUCCESS);
    }

    /**
     * 校验appCode与accessKey是否正确
     *
     * @param request request
     * @return Result
     */
    @PostMapping("/checkAccessKey")
    public ResultResponse checkAccessKey(HttpEntity<Map<String, String>> request, HttpServletRequest httpServletRequest) {
        Map<String, String> queryMap = request.getBody();
        boolean result = seqDesignService.checkAccessKey(queryMap);
        if (!result) {
            return ResultResponse.error(ExceptionEnum.SEQ_ACCESS_KEY_CHECK_FAILED);
        }
        return ResultResponse.success(ResultEnum.SUCCESS);
    }

    /**
     * 动态切换数据库
     *
     * @param sourceInfoEntity sourceInfoEntity
     * @return Result
     */
    @PostMapping("/cutDataSource")
    public ResultResponse cutDataSource(@RequestBody DataSourceInfoEntity sourceInfoEntity) {
        Optional.ofNullable(sourceInfoEntity).orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_DATA_SOURCE_PARAM_EMPTY));
        try {
            for (Field file : sourceInfoEntity.getClass().getDeclaredFields()) {
                file.setAccessible(true);
                Object object = file.get(sourceInfoEntity);
                Optional.ofNullable(object).filter(obj -> !"".equals(obj)).orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_UPDATE_DB_FILE_EMPTY));
            }
            setDataSourceInfo(sourceInfoEntity);
        } catch (IllegalAccessException e) {
            LOGGER.error("数据库切换失败.msg = {}", e.getMessage(), e);
            throw new SequenceException(ExceptionEnum.SEQ_SWITCH_DB_FAILED);
        }
        return ResultResponse.success(ResultEnum.SUCCESS);
    }


    /**
     * 根据集群 切换数据源
     */
    private void setDataSourceInfo(DataSourceInfoEntity sourceInfoEntity) {
        List<DataSourceInfoEntity> list = new ArrayList<>();
        list.add(sourceInfoEntity);
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
        // 使用自己设置的sourceName作为数据源的key，使用这个进行切换
        DataSourceContextHolder.setDBType(sourceInfoEntity.getDatasourceName());
    }

    /**
     * 序号校验
     *
     * @param seqParamsDto seqParamsDto
     * @return SeqDesignPo
     */
    public SeqDesignPo sequenceCodeValidation(SeqParamsDto seqParamsDto) {
        String engineCacheKey = Optional.ofNullable(seqParamsDto)
                .map(SeqParamsDto::engineCacheKey)
                .filter(StringUtils::isNotBlank)
                .orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_ENGINE_CACHE_KEY_NOT_FOUND));
        AbstractSeqFlowEngine seqFlowEngine = SeqHolder.getEngineMap().get(engineCacheKey);
        return Optional.ofNullable(seqFlowEngine)
                .map(AbstractSeqFlowEngine::getSeqObj)
                .map(seqObj -> {
                    SeqDesignPo designPo = new SeqDesignPo();
                    BeanUtils.copyProperties(seqObj, designPo);
                    return designPo;
                })
                .orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_NOT_FOUND));
    }
}
