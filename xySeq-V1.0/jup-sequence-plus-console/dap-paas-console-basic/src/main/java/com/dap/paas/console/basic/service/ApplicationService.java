package com.dap.paas.console.basic.service;

import com.base.core.service.BaseService;
import com.dap.paas.console.basic.entity.Application;

import java.util.Map;

public interface ApplicationService extends BaseService<Application, String> {

    Application getObjectDataByMap(Map map);

    boolean checkAccessKey(Map map);

}
