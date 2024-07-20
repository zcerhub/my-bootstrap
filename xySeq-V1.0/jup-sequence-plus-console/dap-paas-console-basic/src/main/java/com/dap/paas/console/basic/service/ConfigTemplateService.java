package com.dap.paas.console.basic.service;

import com.base.api.dto.Page;
import com.base.core.service.BaseService;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.basic.entity.ConfigTemplate;
import com.dap.paas.console.basic.vo.TemplateDetailVo;
import com.dap.paas.console.basic.vo.TemplateNameIdVo;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public interface ConfigTemplateService extends BaseService<ConfigTemplate, String> {

    /**
     * 查询模板列表
     * @param pageQuery 分页查询条件
     * @return 模板列表
     */
    Page<ConfigTemplate> listObject(PageInDto<ConfigTemplate> pageQuery);

    List<TemplateNameIdVo> listTemplateNameId(ConfigTemplate configTemplate);


    void save(ConfigTemplate configTemplate);

    /**
     * 根据id获取模板详情
     * @param s 模板id
     * @return 模板详情
     */
    TemplateDetailVo getConfigTemplateDetail(String s);

    /**
     * 根据id获取模板
     * @param id 模板id
     * @param tClass 配置类型
     * @param <T> 配置类
     * @return 配置类
     */
    <T> T getConfigFromId(String id, Class<T> tClass);


    <T> T getConfigFromId(String id, TypeReference<T> typeReference);
}
