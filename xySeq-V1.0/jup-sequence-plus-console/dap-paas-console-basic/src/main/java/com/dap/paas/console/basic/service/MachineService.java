package com.dap.paas.console.basic.service;

import com.base.core.service.BaseService;
import com.dap.paas.console.basic.entity.Machine;
import com.dap.paas.console.basic.vo.MachineVo;

import java.io.IOException;

public interface MachineService extends BaseService<Machine, String> {
    boolean checkSsh(Machine machine) throws IOException;
    MachineVo getMachineInfo(Machine machine);
    Integer updateByUnitId(String id) throws Exception;

}
