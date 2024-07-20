package com.dap.paas.console.basic.service.impl;

import com.base.api.dto.Page;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.dto.AuthenticationUserDto;
import com.base.sys.api.dto.PageInDto;
import com.base.sys.api.entity.BaseSysUser;
import com.base.sys.org.service.BaseSysUserService;
import com.base.sys.utils.UserUtil;
import com.dap.paas.console.basic.entity.ConfigEntity;
import com.dap.paas.console.basic.entity.ConfigTemplate;
import com.dap.paas.console.basic.enums.ComponentEnum;
import com.dap.paas.console.basic.enums.ModuleEnum;
import com.dap.paas.console.basic.service.ConfigTemplateService;
import com.dap.paas.console.basic.vo.TemplateDetailVo;
import com.dap.paas.console.basic.vo.TemplateNameIdVo;
import com.dap.paas.console.common.exception.DapWebServerException;
import com.dap.paas.console.common.util.DateUtils;
import com.dap.paas.console.common.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ConfigTemplateServiceImpl extends AbstractBaseServiceImpl<ConfigTemplate, String>
        implements ConfigTemplateService {
    @Autowired
    private BaseSysUserService baseSysUserService;

    private static final TypeReference<List<ConfigEntity>> configTypeReference =
            new TypeReference<List<ConfigEntity>>() {};

    @Override
    public void save(ConfigTemplate configTemplate) {
        if (StringUtils.isBlank(configTemplate.getComponentType())
                || !ComponentEnum.isExist(configTemplate.getComponentType())) {
            throw new DapWebServerException("组件类型不正确");
        }
        if (!ModuleEnum.isExist(configTemplate.getModuleType())) {
            throw new DapWebServerException("模块类型不正确");
        }
        Date currentDate = DateUtils.getCurrentDate();
        AuthenticationUserDto user = UserUtil.getUser();
        if (user == null) {
            throw new DapWebServerException("用户未登录");
        }
        String userId = user.getUserId();
        if (StringUtils.isBlank(configTemplate.getId())) {
            configTemplate.setStatus(0);
            configTemplate.setDeleted(0);
            configTemplate.setCreateDate(currentDate);
            configTemplate.setUpdateDate(currentDate);
            configTemplate.setCreateUserId(userId);
            configTemplate.setUpdateUserId(userId);
            configTemplate.setTenantId(user.getTenantId());
            saveObject(configTemplate);
        } else {
            configTemplate.setUpdateDate(currentDate);
            configTemplate.setUpdateUserId(userId);
            updateObject(configTemplate);
        }
    }

    @Override
    public List<TemplateNameIdVo> listTemplateNameId(ConfigTemplate configTemplate) {
        return queryList(configTemplate).stream().map(t -> {
            TemplateNameIdVo vo = new TemplateNameIdVo();
            vo.setId(t.getId());
            vo.setName(t.getName());
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 查询模板列表
     *
     * @param pageQuery 分页查询条件
     * @return 模板列表
     */
    @Override
    public Page<ConfigTemplate> listObject(PageInDto<ConfigTemplate> pageQuery) {
        Page<ConfigTemplate> page = queryPage(pageQuery.getPageNo(),
                pageQuery.getPageSize(), pageQuery.getRequestObject());
        Map<String, String> map = new HashMap<>();
        page.setData(page.getData().stream().peek(t -> {
            String updateUserId = t.getUpdateUserId();
            if (StringUtils.isNotBlank(updateUserId)) {
                // 从缓存中获取
                if (map.containsKey(updateUserId)) {
                    t.setUpdateUserName(map.get(updateUserId));
                } else {
                    BaseSysUser user = null;
                    try {
                        user = baseSysUserService.getUserById(updateUserId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (user != null) {
                        t.setUpdateUserName(user.getName());
                        map.put(updateUserId, user.getName());
                    } else {
                        // 为null时，设置为空字符串，防止重复查询
                        map.put(updateUserId, "");
                    }
                }
            }
        }).collect(Collectors.toList()));
        return page;
    }

    @SneakyThrows
    @Override
    public TemplateDetailVo getConfigTemplateDetail(String s) {
        ConfigTemplate configTemplate = super.getObjectById(s);
        TemplateDetailVo vo = new TemplateDetailVo();
        if (configTemplate != null) {
            vo.setId(configTemplate.getId());
            vo.setContent(configTemplate.getContent());
            vo.setModuleType(configTemplate.getModuleType());
            vo.setComponentType(configTemplate.getComponentType());
            vo.setName(configTemplate.getName());
        }
        return vo;
    }

    @Override
    public <T> T getConfigFromId(String id, Class<T> tClass) {
        if (StringUtils.isNotBlank(id)) {
            //设置配置模板
            ConfigTemplate template = getObjectById(id);
            if (template != null) {
                String content = template.getContent();
                if (StringUtils.isNotBlank(content)) {
                    List<ConfigEntity> configEntities = JsonUtil.string2Obj(content, configTypeReference);
                    if (CollectionUtils.isNotEmpty(configEntities)) {
                        Map<String, Object> jsonMap = new HashMap<>();
                        configEntities.forEach(entity -> jsonMap.put(entity.getConfigKey(), entity.getConfigValue()));
                        return JsonUtil.string2Obj(JsonUtil.obj2String(jsonMap), tClass);
                    }
                }
            } else {
                log.error("config template is null , id is {}" , id);
            }
        }
        return null;
    }

    @Override
    public <T> T getConfigFromId(String id, TypeReference<T> typeReference) {
        if (StringUtils.isNotBlank(id)) {
            //设置配置模板
            ConfigTemplate template = getObjectById(id);
            if (template != null) {
                String content = template.getContent();
                if (StringUtils.isNotBlank(content)) {
                    return JsonUtil.string2Obj(content, typeReference);
                }
            } else {
                log.error("config template is null , id is {}" , id);
            }
        }
        return null;
    }
}
