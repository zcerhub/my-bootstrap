package com.dap.paas.console.basic.service.impl;

import com.dap.paas.console.basic.dao.ApplicationDao;
import com.dap.paas.console.basic.dao.CityDao;
import com.dap.paas.console.basic.dao.MachineDao;
import com.dap.paas.console.basic.dao.MachineRoomDao;
import com.dap.paas.console.basic.entity.Application;
import com.dap.paas.console.basic.entity.City;
import com.dap.paas.console.basic.entity.Machine;
import com.dap.paas.console.basic.entity.MachineRoom;
import com.dap.paas.console.basic.service.BasicInfoSyncService;
import com.dap.paas.console.basic.utils.RandomUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BasicInfoSyncServiceImpl implements BasicInfoSyncService {

    @Autowired
    private MachineRoomDao machineRoomDao;
    @Autowired
    private MachineDao machineDao;
    @Autowired
    private CityDao cityDao;
    @Autowired
    private ApplicationDao applicationDao;

    @Override
    public void syncMachineRoom(List<MachineRoom> list) {
        List<MachineRoom> existMachineRoom = machineRoomDao.findExist(list);
        if(CollectionUtils.isNotEmpty(existMachineRoom)){
            List<String> existMachineRoomCode = existMachineRoom.stream().map(MachineRoom::getMachineRoomCode).collect(Collectors.toList());
            list =list.stream().filter(x->!existMachineRoomCode.contains(x.getMachineRoomCode())).collect(Collectors.toList());

        }
        if(CollectionUtils.isNotEmpty(list)){
            machineRoomDao.saveMachineRoomBatch(list);
        }
    }

    @Override
    public void syncMachine(List<Machine> list) {
        List<Machine> existMachine = machineDao.findExist(list);
        if(CollectionUtils.isNotEmpty(existMachine)){
            List<String> existMachineIp = existMachine.stream().map(Machine::getHostIp).collect(Collectors.toList());
            list =list.stream().filter(x->!existMachineIp.contains(x.getHostIp())).collect(Collectors.toList());

        }
        if(CollectionUtils.isNotEmpty(list)){
            machineDao.saveMachineBatch(list);
        }
    }

    @Override
    @Transactional
    public void syncCity(List<City> list)  {
        List<City> existCity = cityDao.findExist(list);
        if(CollectionUtils.isNotEmpty(existCity)){
            List<String> existCityCode = existCity.stream().map(City::getCityCode).collect(Collectors.toList());
            list =list.stream().filter(x->!existCityCode.contains(x.getCityCode())).collect(Collectors.toList());

        }
        if(CollectionUtils.isNotEmpty(list)){
            cityDao.saveCityBatch(list);
        }
    }

    @Override
    public void syncApplication(List<Application> list) {
        List<Application> existApplication = applicationDao.findExist(list);
        if(CollectionUtils.isNotEmpty(existApplication)){
            List<String> existApplicationCode = existApplication.stream().map(Application::getApplicationCode).collect(Collectors.toList());
            list =list.stream().filter(x->!existApplicationCode.contains(x.getApplicationCode())).collect(Collectors.toList());

        }
        if(CollectionUtils.isNotEmpty(list)){
            list.forEach(l->{
                l.setAccessKey(RandomUtil.generateSecureAccessKey(10));
                l.setAccessKeyStatus("1");
            });
            applicationDao.saveApplicationBatch(list);
        }
    }

}
