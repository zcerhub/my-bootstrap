package com.dap.paas.console.seq.controller;

import com.base.api.dto.Page;
import com.base.sys.api.common.Result;
import com.base.sys.api.dto.PageInDto;
import com.base.sys.auth.log.AuditLog;
import com.base.sys.auth.log.OperateTypeAspect;
import com.dap.paas.console.basic.service.ApplicationService;
import com.dap.paas.console.common.exception.ExceptionEnum;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.seq.check.CollectionValue;
import com.dap.paas.console.seq.check.ValidGroup;
import com.dap.paas.console.seq.entity.SeqDesignPo;
import com.dap.paas.console.seq.feign.SeqDesignFeignApi;
import com.dap.paas.console.seq.feign.SeqInstanceRuleFeignApi;
import com.dap.paas.console.seq.service.SeqDesignService;
import com.dap.paas.console.seq.util.SequenceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import java.util.*;

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
    private SeqInstanceRuleFeignApi seqInstanceRuleFeignApi;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private SeqDesignFeignApi seqDesignFeignApi;

/*
    @Autowired
    private SeqSdkMonitorDao seqSdkMonitorDao;


    @Autowired
    private SeqDesignDao seqDesignDao;

    @Autowired
    private RestTemplate restTemplate;
*/

    /**
     * 序列新增
     *
     * @param seqDesignPo seqDesignPo
     * @return Result
     */
    @ApiOperation(value = "序列新增")
    @PostMapping("/insert")
    @AuditLog(modelName = "序列设计器管理", operateType = OperateTypeAspect.POST, componentType = OperateTypeAspect.SEQ)
    public ResultResponse<ExceptionEnum> insert(@RequestBody @Validated(value = {ValidGroup.Valid.Create.class}) SeqDesignPo seqDesignPo) {
        return seqDesignService.insert(seqDesignPo);
    }

    /**
     * 序列列表查询
     *
     * @param param param
     * @return Result
     */
    @ApiOperation("序列设计管理列表")
    @PostMapping("/queryPage")
    public ResultResponse<Page<SeqDesignPo>> queryPage(@RequestBody PageInDto<SeqDesignPo> param) {
        return seqDesignService.queryPage(param);
    }

    /**
     * 序列修改
     *
     * @param seqDesignPo seqDesignPo
     * @return Result
     */
    @ApiOperation(value = "序列设计管理编辑")
    @PutMapping("/update")
    public ResultResponse<ExceptionEnum> update(@RequestBody @Validated(value = {ValidGroup.Valid.Update.class}) SeqDesignPo seqDesignPo) {
        return seqDesignService.update(seqDesignPo);
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
    public ResultResponse<ExceptionEnum> delete(@PathVariable("id") @Pattern(regexp = "^\\d{1,32}$", message = "序列id不合法！") String id) {
        return seqDesignService.delete(id);
    }

    /**
     * 序列发布或者下线
     *
     * @param seqDesignPo seqDesignPo
     * @return Result
     */
    @ApiOperation("单个发布和下线")
    @PutMapping("/issue")
    public ResultResponse<ExceptionEnum> issue(@RequestBody @Validated(value = {ValidGroup.Valid.SeqDesignIssue.class}) SeqDesignPo seqDesignPo) {
        return seqDesignService.issue(seqDesignPo);
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
    public ResultResponse<ExceptionEnum> batchIssue(@RequestBody @Valid List<SeqDesignPo> seqDesignPos) {
        return seqDesignService.batchIssue(seqDesignPos);
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
    public void export(HttpServletResponse response, @RequestBody @NotEmpty(message = "序列id不能为空！") @CollectionValue(regex = "^\\d{1,32}$", message = "id不合法") Set<String> ids) {
        ResultResponse<List<SeqDesignPo>> resultResponse = seqDesignFeignApi.queryListByIds(ids);
        List<SeqDesignPo> seqDesignList = (List<SeqDesignPo>) resultResponse.getData();
        SequenceUtil sequenceUtil = new SequenceUtil(seqDesignService, seqInstanceRuleFeignApi);
        seqDesignList.forEach(sequenceUtil::doConfigRule);
        seqDesignService.exportExcel(response, seqDesignList);
    }

    /**
     * 序列导入
     *
     * @param file file
     * @return Result
     */
    @ApiOperation("导入序列设计管理列表")
    @PostMapping(value = "/import", produces = {"application/json;charset=UTF-8"})
    public ResultResponse<ExceptionEnum> upload(@RequestBody @RequestParam("file") MultipartFile file) {
        List<SeqDesignPo> dataList = seqDesignService.readExcel(file);
        return seqDesignService.saveBatch(dataList);
    }
    /**
     * 推送序列规则
     *
     * @param List<String> codes
     * @return Result
     */
    @ApiOperation("推送序列规则")
    @PostMapping(value = "/pushRules")
    public Result pushRules(@RequestBody List<String> codes) {
        return  seqDesignService.pushRules(codes);
    }

}
