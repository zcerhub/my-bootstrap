package com.base.sys.menu.service;

import com.base.core.service.BaseService;
import com.base.sys.api.entity.BaseSysApp;

import javax.servlet.http.HttpServletResponse;

public interface BaseSysAppService extends BaseService<BaseSysApp, String> {

    void watchInstructionBookById(String id, HttpServletResponse response);


}
