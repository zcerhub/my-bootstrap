package com.dap.paas.console.basic.service.impl;

import com.base.api.entity.BaseEntity;
import com.base.api.exception.DaoException;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.entity.TenantManageEntity;
import com.base.sys.tenant.dao.TenantManageDao;
import com.dap.paas.console.basic.dao.BasicSyncDao;
import com.dap.paas.console.basic.entity.BasicSync;
import com.dap.paas.console.basic.entity.City;
import com.dap.paas.console.basic.entity.Machine;
import com.dap.paas.console.basic.entity.MachineRoom;
import com.dap.paas.console.basic.entity.Organization;
import com.dap.paas.console.basic.entity.Unitization;
import com.dap.paas.console.basic.service.BasicSyncService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;


@Service
public class BasicSyncServiceImpl extends AbstractBaseServiceImpl<BasicSync,String> implements BasicSyncService {

    @Autowired
    private BasicSyncDao basicSyncDao;

    @Autowired
    private TenantManageDao tenantManageDao;

    private static final Logger log= LoggerFactory.getLogger(BasicSyncServiceImpl.class);

    private void setTenant(BaseEntity entity) {
        if(entity.getTenantId() == null) {
            try {
                List<TenantManageEntity> tenant = tenantManageDao.queryList(new HashMap());
                if(tenant != null && !tenant.isEmpty()) {
                    String tenantId = tenantManageDao.getIdByCodeOnLogin(tenant.get(0).getTenantCode());
                    entity.setTenantId(tenantId);
                }

            } catch (DaoException e) {
                log.error(e.getMessage());
            }
        }
    }

    @Override
    //@Transactional(rollbackFor=Exception.class)
    public Result syncCity(City city) {
        try {
            List<City> c = basicSyncDao.getCityByName(city.getCityName());
            if(c == null || c.isEmpty()) {
                setTenant(city);
                basicSyncDao.saveCity(city);
            }
            else {
                city.setId(c.get(0).getId());
            }
            return ResultUtils.success();
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultUtils.error("同步数据失败");
        }
    }

    @Override
    public Result updateCity(City city) {
        try {
            setTenant(city);
            basicSyncDao.updateCity(city);
            return ResultUtils.success();
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultUtils.error("同步数据失败");
        }
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public Result syncOrganization(List<Organization> organizationList) {
        try {
            basicSyncDao.clearOrganization();
            if (CollectionUtils.isEmpty(organizationList)){
                return ResultUtils.success();
            }
            for (Organization organization : organizationList){
                basicSyncDao.saveOrganization(organization);
            }
            return ResultUtils.success();
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultUtils.error("同步数据失败");
        }
    }

    @Override
    public MachineRoom getDatacenter(String id) {
        try {
            MachineRoom room = basicSyncDao.getMachineRoomById(id);
            return room;
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public Result syncDatacenter(List<MachineRoom> machineRoomList) {
        try {
//            basicSyncDao.clearMachineRoom();
//            if (CollectionUtils.isEmpty(machineRoomList)){
//                return ResultUtils.success();
//            }
            for (MachineRoom machineRoom : machineRoomList){
                MachineRoom room = basicSyncDao.getMachineRoomById(machineRoom.getId());
                if(room == null) {
                    setTenant(machineRoom);
                    basicSyncDao.saveMachineRoom(machineRoom);
                }
            }
            return ResultUtils.success();
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultUtils.error("同步数据失败");
        }
    }

    @Override
    public Result updateDatacenter(MachineRoom machineRoom) {
        try {
            setTenant(machineRoom);
            basicSyncDao.updateMachineRoom(machineRoom);
            return ResultUtils.success();
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultUtils.error("同步数据失败");
        }
    }

    @Override
    public Result deleteDatacenter(String id) {
        try {
            basicSyncDao.delMachineRoomById(id);
            return ResultUtils.success();
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultUtils.error("同步数据失败");
        }
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public Result syncMachine(List<Machine> machineList) {
        try {
//            basicSyncDao.clearMachine();
//            if (CollectionUtils.isEmpty(machineList)){
//                return ResultUtils.success();
//            }
            for (Machine machine : machineList){
                if(machine.getHostSshAccount().equals("root")) {
                    machine.setDeploymentPath("/");
                } else {
                    machine.setDeploymentPath("/home/" + machine.getHostSshAccount());
                }

                Machine m = basicSyncDao.getMachineById(machine.getId());
                if(m == null) {
                    setTenant(machine);
                    basicSyncDao.saveMachine(machine);
                }
            }
            return ResultUtils.success();
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultUtils.error("同步数据失败");
        }
    }

    @Override
    public Result updateMachine(Machine machine) {
        try {
            setTenant(machine);
            basicSyncDao.updateMachine(machine);
            return ResultUtils.success();
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultUtils.error("同步数据失败");
        }
    }

    @Override
    public Result deleteMachine(String id) {
        try {
            basicSyncDao.delMachineById(id);
            return ResultUtils.success();
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultUtils.error("同步数据失败");
        }
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public Result syncUnitization(List<Unitization> unitizationList) {
        try {
//            basicSyncDao.clearUnitization();
//            if (CollectionUtils.isEmpty(unitizationList)){
//                return ResultUtils.success();
//            }
            for (Unitization unitization : unitizationList){
                unitization.setUnitName(unitization.getUnitCode() + "-" + unitization.getBakNo());
                Unitization u = basicSyncDao.getUnitizationById(unitization.getId());
                if(u == null) {
                    setTenant(unitization);
                    basicSyncDao.saveUnitization(unitization);
                }

            }
            return ResultUtils.success();
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultUtils.error("同步数据失败");
        }
    }

    @Override
    public Result updateUnitization(Unitization unitization) {
        try {
            setTenant(unitization);
            basicSyncDao.updateUnitization(unitization);
            return ResultUtils.success();
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultUtils.error("同步数据失败");
        }
    }

    @Override
    public Result deleteUnitization(String id) {
        try {
            basicSyncDao.delUnitizationById(id);
            return ResultUtils.success();
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultUtils.error("同步数据失败");
        }
    }

}
