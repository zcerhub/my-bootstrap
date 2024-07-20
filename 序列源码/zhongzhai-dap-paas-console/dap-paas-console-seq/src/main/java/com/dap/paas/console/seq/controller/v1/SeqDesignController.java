package com.dap.paas.console.seq.controller.v1;

import com.base.api.dto.Page;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.base.sys.auth.log.AuditLog;
import com.base.sys.auth.log.OperateTypeAspect;
import com.dap.paas.console.basic.entity.Application;
import com.dap.paas.console.basic.service.ApplicationService;
import com.dap.paas.console.seq.check.CollectionValue;
import com.dap.paas.console.seq.check.ValidGroup;
import com.dap.paas.console.seq.constant.SeqConstant;
import com.dap.paas.console.seq.entity.SeqDesignPo;
import com.dap.paas.console.seq.entity.SeqInstanceRule;
import com.dap.paas.console.seq.service.SeqDesignService;
import com.dap.paas.console.seq.service.SeqInstanceRuleService;
import com.dap.paas.console.seq.util.SequenceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @className SeqDesignController
 * @description 序列设计管理
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Api(tags = "序列设计管理")
@RestController
@RequestMapping("/v1/seq/design")
@Validated
public class SeqDesignController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeqDesignController.class);

    @Autowired
    private SeqDesignService seqDesignService;

    @Autowired
    private SeqInstanceRuleService seqInstanceRuleService;

    @Autowired
    private ApplicationService applicationService;

    /**
     * 序列新增
     *
     * @param seqDesignPo seqDesignPo
     * @return Result
     */
    @ApiOperation(value = "序列新增")
    @PostMapping("/insert")
    @AuditLog(modelName = "序列设计器管理", operateType = OperateTypeAspect.POST, componentType = OperateTypeAspect.SEQ)
    public Result insert(@RequestBody @Validated(value = {ValidGroup.Valid.Create.class}) SeqDesignPo seqDesignPo) {
        LOGGER.info("序列新增信息 = {}", seqDesignPo);
        SeqDesignPo query = new SeqDesignPo();
        query.setSequenceCode(seqDesignPo.getSequenceCode());
        query.setSequenceName(seqDesignPo.getSequenceName());
        List<SeqDesignPo> list = seqDesignService.queryList(query);
        if (CollectionUtils.isNotEmpty(list)) {
            return ResultUtils.error(ResultEnum.FAILED.getCode(), "序列已存在");
        }
        seqDesignPo.setSequenceStatus(SeqConstant.SEQ_SERVER_ONE);
        seqDesignService.saveObject(seqDesignPo);
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * 序列列表查询
     *
     * @param param param
     * @return Result
     */
    @ApiOperation("序列设计管理列表")
    @PostMapping("/queryPage")
    public Result queryPage(@RequestBody PageInDto<SeqDesignPo> param) {
        Page seqDesignList = seqDesignService.queryPage(param.getPageNo(), param.getPageSize(), param.getRequestObject());
        List<SeqDesignPo> clusterlist = seqDesignList.getData();
        for (SeqDesignPo seqDesignPo : clusterlist) {
            HashMap<String, String> map = new HashMap<>();
            map.put("seqDesignId", seqDesignPo.getId());
            String instanceRule = seqInstanceRuleService.getInstanceRuleList(map).stream().map(SeqInstanceRule::getInstanceRuleName).collect(Collectors.joining(";"));
            seqDesignPo.setSequenceRule(instanceRule);
        }
        return ResultUtils.success(ResultEnum.SUCCESS, seqDesignList);
    }

    /**
     * 序列修改
     *
     * @param seqDesignPo seqDesignPo
     * @return Result
     */
    @ApiOperation(value = "序列设计管理编辑")
    @PutMapping("/update")
    public Result update(@RequestBody @Validated(value = {ValidGroup.Valid.Update.class}) SeqDesignPo seqDesignPo) {
        LOGGER.info("序列设计管理编辑信息 = {}", seqDesignPo);
        SeqDesignPo query = new SeqDesignPo();
        query.setId(seqDesignPo.getId());
        query.setSequenceName(seqDesignPo.getSequenceName());
        query.setSequenceCode(seqDesignPo.getSequenceCode());
        List<SeqDesignPo> list = seqDesignService.queryList(query);
        if (CollectionUtils.isEmpty(list)) {
            return ResultUtils.error(ResultEnum.FAILED.getCode(), "序列不存在");
        }
        seqDesignService.updateObject(seqDesignPo);
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * 序列删除
     *
     * @param id id
     * @return Result
     */
    @ApiOperation(value = "序列删除")
    @DeleteMapping("/delete/{id}")
    @Transactional(rollbackFor = Exception.class)
    public Result delete(@PathVariable("id") @Pattern(regexp = "^\\d{1,32}$", message = "序列id不合法！") String id) {
        LOGGER.info("序列删除id = {}", id);
        Optional.ofNullable(id).filter(StringUtils::isNotBlank).ifPresent(i -> {
            seqDesignService.delObjectById(i);
            seqInstanceRuleService.delObjectByDesignId(i);
        });
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * 序列发布或者下线
     *
     * @param seqDesignPo seqDesignPo
     * @return Result
     */
    @ApiOperation("单个发布和下线")
    @PutMapping("/issue")
    public Result issue(@RequestBody @Validated(value = {ValidGroup.Valid.SeqDesignIssue.class}) SeqDesignPo seqDesignPo) {
        LOGGER.info("单个发布和下线信息 = {}", seqDesignPo);
        String appId = seqDesignPo.getSequenceApplicationId();
        if (StringUtils.isNotEmpty(appId)) {
            Application application = applicationService.getObjectById(appId);
            if (Objects.isNull(application)) {
                return ResultUtils.error(ResultEnum.FAILED.getCode(), "应用不存在");
            }
            seqDesignPo.setAccessKey(application.getAccessKey());
            seqDesignPo.setAppCode(application.getApplicationCode());
        }
        if ("2".equals(seqDesignPo.getSequenceStatus())) {
            Map<String, String> map = new HashMap<>();
            map.put("seqDesignId", seqDesignPo.getId());
            if (seqInstanceRuleService.queryList(map).isEmpty()) {
                return ResultUtils.error(ResultEnum.FAILED.getCode(), "规则不能为空");
            }
            seqDesignService.updateObject(seqDesignPo);
        } else {
            seqDesignService.updateObject(seqDesignPo);
        }
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * 批量发布和下线
     *
     * @param seqDesignPos seqDesignPos
     * @return Result
     */
    @ApiOperation("批量发布和下线")
    @PutMapping("/batchIssue")
    @Transactional(rollbackFor = Exception.class)
    public Result batchIssue(@RequestBody @Valid List<SeqDesignPo> seqDesignPos) {
        LOGGER.info("批量发布和下线信息 = {}", seqDesignPos);
        // 校验应用是否存在
        Set<String> appIds = seqDesignPos.stream().map(SeqDesignPo::getSequenceApplicationId).collect(Collectors.toSet());
        List<Application> applicationList = applicationService.queryListByIds(appIds);
        if (CollectionUtils.isEmpty(applicationList)) {
            return ResultUtils.error(ResultEnum.FAILED.getCode(), "所有应用不存在");
        }
        for (Application application : applicationList) {
            if (!appIds.contains(application.getId())) {
                return ResultUtils.error(ResultEnum.FAILED.getCode(), "应用" + application.getApplicationCode() + "不存在");
            }
        }
        seqDesignPos.forEach(x -> {
            applicationList.stream()
                    .filter(item -> item.getId().equals(x.getSequenceApplicationId()))
                    .findFirst().ifPresent(app -> {
                        x.setAppCode(app.getApplicationCode());
                        x.setAccessKey(app.getAccessKey());
                    });
        });

        // 校验发布和下线状态一致，防止选中的数据存在两个状态值“已发布、未发布”
        List<String> seqIds = new ArrayList<>();
        Map<String, String> statusMap = new HashMap<>();
        for (SeqDesignPo seqDesignPo : seqDesignPos) {
            String status = seqDesignPo.getSequenceStatus();
            if (StringUtils.isNotEmpty(status)) {
                statusMap.put(status, "");
            } else {
                return ResultUtils.error(ResultEnum.FAILED.getCode(), "状态不能为空");
            }
            seqIds.add(seqDesignPo.getId());
        }
        if (statusMap.size() > 1) {
            return ResultUtils.error(ResultEnum.FAILED.getCode(), "状态不一致，请重新选择");
        }

        Set<String> statuskeySet = statusMap.keySet();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("ids", seqIds);
        if (statuskeySet.contains(SeqConstant.SEQ_DESIGN_STATUS_RUN)) {
            for (SeqDesignPo seqDesignPo : seqDesignPos) {
                Map<String, String> map = new HashMap<>();
                map.put("seqDesignId", seqDesignPo.getId());
                if (seqInstanceRuleService.queryList(map).isEmpty()) {
                    SeqDesignPo seqDesPo = seqDesignService.getObjectById(seqDesignPo.getId());
                    if (seqDesPo != null) {
                        String seqName = seqDesPo.getSequenceName();
                        return ResultUtils.error(ResultEnum.FAILED.getCode(), "序列【" + seqName + "】的规则不能为空");
                    } else {
                        return ResultUtils.error(ResultEnum.FAILED.getCode(), "序列不存在");
                    }
                }
            }
            // 发布
            paramMap.put("status", SeqConstant.SEQ_DESIGN_STATUS_RUN);
            seqDesignService.updateStatusByIds(paramMap);
        } else if (statuskeySet.contains(SeqConstant.SEQ_DESIGN_STATUS_STOP)) {
            // 下线
            paramMap.put("status", SeqConstant.SEQ_DESIGN_STATUS_STOP);
            seqDesignService.updateStatusByIds(paramMap);
        } else {
            return ResultUtils.error(ResultEnum.FAILED.getCode(), "入参中状态，参数错误");
        }

        // 修改seq_design表信息
        seqDesignPos.forEach(po -> seqDesignService.updateObject(po));
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * 序列导出
     *
     * @param response response
     * @param ids ids
     * @return Result
     */
    @ApiOperation("下载选中的序列设计管理列表excel")
    @PostMapping("/export")

    public Result export(HttpServletResponse response, @RequestBody @NotEmpty(message = "序列id不能为空！") @CollectionValue(regex = "^\\d{1,32}$", message = "id不合法") Set<String> ids) {
        List<SeqDesignPo> seqDesignList = seqDesignService.queryListByIds(ids);
        SequenceUtil sequenceUtil = new SequenceUtil(seqDesignService, seqInstanceRuleService);
        seqDesignList.forEach(sequenceUtil::doConfigRule);
        seqDesignService.exportExcel(response, seqDesignList);
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * 序列导入
     *
     * @param file file
     * @return Result
     */
    @ApiOperation("导入序列设计管理列表")
    @PostMapping(value = "/import", produces = {"application/json;charset=UTF-8"})
    public Result upload(@RequestBody @RequestParam("file") MultipartFile file) {
        List<SeqDesignPo> dataList = seqDesignService.readExcel(file);
        return seqDesignService.saveBatch(dataList);
    }
}
