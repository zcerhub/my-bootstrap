package com.dap.paas.console.basic.service;

import com.base.core.service.BaseService;
import com.dap.paas.console.basic.entity.MachineRoom;

public interface MachineRoomService extends BaseService<MachineRoom, String> {
    boolean syncMachineRoom();
}
