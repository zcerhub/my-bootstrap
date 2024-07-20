package com.dap.paas.console.seq.service.impl;


import cn.hutool.core.map.MapUtil;
import com.base.api.dto.Page;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.common.exception.ExceptionEnum;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.common.util.ExcelUtil;
import com.dap.paas.console.seq.entity.SeqTemplateRule;
import com.dap.paas.console.seq.feign.SeqTemplateRuleFeignApi;
import com.dap.paas.console.seq.service.SeqTemplateRuleService;
import com.dap.paas.console.seq.util.SequenceUtil;


import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @className SeqTemplateRuleServiceImpl
 * @description 序列模板规则实现
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Service
public class SeqTemplateRuleServiceImpl implements SeqTemplateRuleService {

    private static final Map<String, String> TITLE_MAP = new LinkedHashMap<>();

    @Autowired
    private SeqTemplateRuleFeignApi seqTemplateRuleFeignApi;

    static {
        TITLE_MAP.put("规则名称", "ruleName");
        TITLE_MAP.put("规则类型", "ruleType");
        TITLE_MAP.put("规则描述", "ruleDesc");
        TITLE_MAP.put("规则信息", "ruleInfo");
    }

    @Override
    public Integer queryTempTotal() {
        ResultResponse<List<SeqTemplateRule>> listResultResponse = seqTemplateRuleFeignApi.queryList(new SeqTemplateRule());
        List<SeqTemplateRule> seqTemplateRules = listResultResponse.getData();
        return Optional.ofNullable(seqTemplateRules).map(List::size).orElse(0);
    }

    @Override
    public void exportExcel(HttpServletResponse response, List<SeqTemplateRule> seqTemplateRuleList) {
        // 需要写入excel的属性及映射字段名
        Map<String, String> keyMap = MapUtil.inverse(TITLE_MAP);

        // 创建sheet，属性映射字段名作为首行名
        Workbook wb = ExcelUtil.creatExcelSheets(Collections.singletonList("1"), new ArrayList<>(keyMap.values()));

        // 根据sheetName写入数据
        ExcelUtil.putData(wb, "1", ExcelUtil.convertListToMapList(seqTemplateRuleList, keyMap, SeqTemplateRule.class));

        // 下载excel
        ExcelUtil.writeExcel(wb, response, "规则定义管理");
    }

    @Override
    public List<SeqTemplateRule> readExcel(MultipartFile file) {
        return ExcelUtil.readExcelToList(file, TITLE_MAP, SeqTemplateRule.class, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveBatch(List<SeqTemplateRule> seqTemplateRuleList) {
        for (SeqTemplateRule seqTemplateRule : seqTemplateRuleList) {
            if (StringUtils.isBlank(seqTemplateRule.getRuleName())) {
                return ResultUtils.error(ResultEnum.INVALID_REQUEST.getCode(), "规则名称不可为空!");
            } else if (StringUtils.isBlank(seqTemplateRule.getRuleType())) {
                return ResultUtils.error(ResultEnum.INVALID_REQUEST.getCode(), "请选择规则类型!");
            } else if (StringUtils.isNotBlank(seqTemplateRule.getRuleDesc()) && seqTemplateRule.getRuleDesc().length() > 64) {
                return ResultUtils.error(ResultEnum.INVALID_REQUEST.getCode(), "系统描述过长，不能超过64长度!");
            }
            Map<String, String> queryMap = new HashMap<>();
            queryMap.put("ruleName", seqTemplateRule.getRuleName());
            queryMap.put("ruleType", seqTemplateRule.getRuleType());
            SequenceUtil.getTenantId().filter(StringUtils::isNotBlank).ifPresent(tenantId -> queryMap.put("tenantId", tenantId));
            ResultResponse<SeqTemplateRule> resultResponse = seqTemplateRuleFeignApi.getObjectByMap(queryMap);
            SeqTemplateRule existSeq = resultResponse.getData();
            if (Objects.nonNull(existSeq)) {
                return ResultUtils.error(ResultEnum.INVALID_REQUEST.getCode(), "该规则已存在，请修改后再操作!");
            }
            seqTemplateRuleFeignApi.saveBatch(Lists.newArrayList(seqTemplateRule));
        }
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    @Override
    public ResultResponse<ExceptionEnum> insert(SeqTemplateRule seqTemplateRule) {
        return seqTemplateRuleFeignApi.insert(seqTemplateRule);
    }

    @Override
    public ResultResponse<Page<SeqTemplateRule>> queryPage(PageInDto<SeqTemplateRule> param) {

        return seqTemplateRuleFeignApi.queryPage(param);
    }

    @Override
    public ResultResponse<ExceptionEnum> update(SeqTemplateRule seqTemplateRule) {

        return seqTemplateRuleFeignApi.update(seqTemplateRule);
    }

    @Override
    public ResultResponse<ExceptionEnum> updateRuleInfo(SeqTemplateRule seqTemplateRule) {

        return seqTemplateRuleFeignApi.updateRuleInfo(seqTemplateRule);
    }

    @Override
    public ResultResponse<ExceptionEnum> delete(String id) {

        return seqTemplateRuleFeignApi.delete(id);
    }

    @Override
    public ResultResponse<ExceptionEnum> batchDelete(List<String> ids) {

        return seqTemplateRuleFeignApi.batchDelete(ids);
    }

    @Override
    public ResultResponse<List<SeqTemplateRule>> queryListByType(String ruleType) {

        return seqTemplateRuleFeignApi.queryListByType(ruleType);
    }
}

