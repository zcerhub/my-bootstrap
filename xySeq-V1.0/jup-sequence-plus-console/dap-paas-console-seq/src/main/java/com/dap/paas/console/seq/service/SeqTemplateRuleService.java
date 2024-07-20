package com.dap.paas.console.seq.service;


import com.base.api.dto.Page;
import com.base.core.service.BaseService;
import com.base.sys.api.common.Result;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.common.exception.ExceptionEnum;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.seq.entity.SeqTemplateRule;


import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @className SeqTemplateRuleService
 * @description 序列模板规则接口
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public interface SeqTemplateRuleService {

    /**
     * 获取模板总数
     *
     * @return Integer
     */
    Integer queryTempTotal();

    /**
     * 导出excel
     *
     * @param response response
     * @param seqTemplateRuleList seqTemplateRuleList
     */
    void exportExcel(HttpServletResponse response, List<SeqTemplateRule> seqTemplateRuleList);

    /**
     * 读取excel内容
     *
     * @param file file
     * @return List
     */
    List<SeqTemplateRule> readExcel(MultipartFile file);

    /**
     * 批量保存
     *
     * @param seqTemplateRuleList seqTemplateRuleList
     * @return Result
     */
    Result saveBatch(List<SeqTemplateRule> seqTemplateRuleList);

    /**
     * 序列规则新增
     *
     * @param seqTemplateRule seqTemplateRule
     * @return Result
     */
    ResultResponse<ExceptionEnum> insert(SeqTemplateRule seqTemplateRule);

    /**
     * 序列规则列表分页查询
     *
     * @param param param
     * @return Result
     */
    ResultResponse<Page<SeqTemplateRule>> queryPage(PageInDto<SeqTemplateRule> param);

    /**
     * 序列规则管理编辑
     *
     * @param seqTemplateRule seqTemplateRule
     * @return Result
     */
    ResultResponse<ExceptionEnum> update(SeqTemplateRule seqTemplateRule);

    /**
     * 序列规则配置
     *
     * @param seqTemplateRule seqTemplateRule
     * @return Result
     */
    ResultResponse<ExceptionEnum> updateRuleInfo(SeqTemplateRule seqTemplateRule);

    /**
     * 序列规则管理删除
     *
     * @param id id
     * @return Result
     */
    ResultResponse<ExceptionEnum> delete(String id);

    /**
     * 序列规则管理-批量删除
     *
     * @param ids ids
     * @return Result
     */
    ResultResponse<ExceptionEnum> batchDelete(List<String> ids);

    /**
     * 根据类型查询所有的模板
     *
     * @param ruleType ruleType
     * @return Result
     */
    ResultResponse<List<SeqTemplateRule>> queryListByType(String ruleType);
}

