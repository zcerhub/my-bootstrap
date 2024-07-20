package com.dap.paas.console.basic.dao;

import com.base.api.exception.DaoException;
import com.base.core.dao.BaseDao;
import com.dap.paas.console.basic.entity.*;

import java.util.List;

public interface BasicSyncDao extends BaseDao<BasicSync, String> {

    List<City> getCityByName(String cityName) throws DaoException;
    Integer updateCity(City city) throws DaoException;
    /**
     * 保存城市数据
     * @return
     */
    public Integer saveCity(City city) throws DaoException;

    /**
     * 清空城市数据
     * @return
     */
    public Integer clearCity() throws DaoException;

    MachineRoom getMachineRoomById(String id) throws DaoException;
    Integer updateMachineRoom(MachineRoom machineRoom) throws DaoException;
    Integer delMachineRoomById(String id) throws DaoException;
    /**
     * 保存数据中心数据
     */
    public Integer saveMachineRoom(MachineRoom machineRoom) throws DaoException;

    /**
     * 清空数据中心数据
     */
    public Integer clearMachineRoom() throws DaoException;

    /**
     * 保存组织数据
     */
    public Integer saveOrganization(Organization organization) throws DaoException;
    /**
     * 清空组织数据
     */
    public Integer clearOrganization() throws DaoException;

    Machine getMachineById(String id) throws DaoException;
    Integer updateMachine(Machine machine) throws DaoException;
    Integer delMachineById(String id) throws DaoException;
    /**
     * 保存机房数据
     */
    public Integer saveMachine(Machine machine) throws DaoException;
    /**
     * 清空机房数据
     */
    public Integer clearMachine() throws DaoException;

    Unitization getUnitizationById(String id) throws DaoException;
    Integer updateUnitization(Unitization unitization) throws DaoException;
    Integer delUnitizationById(String id) throws DaoException;
    /**
     * 保存单元数据
     */
    public Integer saveUnitization(Unitization unitization) throws DaoException;
    /**
     * 清空单元数据
     */
    public Integer clearUnitization() throws DaoException;
}
