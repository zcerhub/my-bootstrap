package com.dap.paas.console.basic.dao.impl;

import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.paas.console.basic.dao.ConfigTemplateDao;
import com.dap.paas.console.basic.entity.ConfigTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ConfigTemplateDaoImpl extends AbstractBaseDaoImpl<ConfigTemplate, String>
        implements ConfigTemplateDao {

}
