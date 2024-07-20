package com.dap.paas.console.seq.service;


import com.base.core.service.BaseService;
import com.base.sys.api.common.Result;
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
public interface SeqTemplateRuleService extends BaseService<SeqTemplateRule, String> {

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
}

