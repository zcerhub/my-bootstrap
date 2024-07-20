package com.dap.paas.console.basic.dao.impl;


import com.base.api.exception.DaoException;
import com.base.api.util.SnowflakeIdWorker;
import com.base.api.util.SqlSessionMapperLabel;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.paas.console.basic.dao.BasicSyncDao;
import com.dap.paas.console.basic.entity.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class BasicSyncDaoImpl extends AbstractBaseDaoImpl<BasicSync,String> implements BasicSyncDao {

    @Override
    public Integer saveCity(City city) throws DaoException{
        int result;
        Date date = new Date();
        city.setCreateDate(date);
        city.setUpdateDate(date);
        city.setId(SnowflakeIdWorker.getID());
        try {
            result = this.getSqlSession().insert(getSqlNamespace().concat(".saveCity"), city);
        }catch ( Exception e){
            throw new DaoException("saveCity方法调用异常："+e.getMessage());
        }
        return result;
    }

    @Override
    public Integer clearCity() throws DaoException{
        int result;
        try {
            result = this.getSqlSession().delete(getSqlNamespace().concat(".clearCity"));
        }catch ( Exception e){
            throw new DaoException("clearCity方法调用异常："+e.getMessage());
        }
        return result;
    }

    @Override
    public Integer saveMachineRoom(MachineRoom machineRoom) throws DaoException{
        int result;
        Date date = new Date();
        machineRoom.setCreateDate(date);
        machineRoom.setUpdateDate(date);
        try {
            result = this.getSqlSession().insert(getSqlNamespace().concat(".saveMachineRoom"), machineRoom);
        }catch ( Exception e){
            throw new DaoException("saveMachineRoom方法调用异常："+e.getMessage());
        }
        return result;
    }

    @Override
    public Integer clearMachineRoom() throws DaoException{
        int result;
        try {
            result = this.getSqlSession().delete(getSqlNamespace().concat(".clearMachineRoom"));
        }catch ( Exception e){
            throw new DaoException("clearMachineRoom方法调用异常："+e.getMessage());
        }
        return result;
    }

    @Override
    public Integer saveOrganization(Organization organization) throws DaoException{
        int result;
        organization.setId(SnowflakeIdWorker.getID());
        Date date = new Date();
        organization.setCreateDate(date);
        organization.setUpdateDate(date);
        try {
            result = this.getSqlSession().insert(getSqlNamespace().concat(".saveOrganization"), organization);
        }catch ( Exception e){
            throw new DaoException("saveOrganization方法调用异常："+e.getMessage());
        }
        return result;
    }
    @Override
    public Integer clearOrganization() throws DaoException{
        int result;
        try {
            result = this.getSqlSession().insert(getSqlNamespace().concat(".clearOrganization"));
        }catch ( Exception e){
            throw new DaoException("clearOrganization方法调用异常："+e.getMessage());
        }
        return result;
    }

    @Override
    public Integer saveMachine(Machine machine) throws DaoException{
        int result;
        Date date = new Date();
        machine.setCreateDate(date);
        machine.setUpdateDate(date);
        try {
            result = this.getSqlSession().insert(getSqlNamespace().concat(".saveMachine"), machine);
        }catch ( Exception e){
            throw new DaoException("saveMachine方法调用异常："+e.getMessage());
        }
        return result;
    }

    @Override
    public Integer clearMachine() throws DaoException{
        int result;
        try {
            result = this.getSqlSession().insert(getSqlNamespace().concat(".clearMachine"));
        }catch ( Exception e){
            throw new DaoException("clearMachine方法调用异常："+e.getMessage());
        }
        return result;
    }

    @Override
    public Integer saveUnitization(Unitization unitization) throws DaoException{
        int result;
        Date date = new Date();
        unitization.setCreateDate(date);
        unitization.setUpdateDate(date);
        try {
            result = this.getSqlSession().insert(getSqlNamespace().concat(".saveUnitization"), unitization);
        }catch ( Exception e){
            throw new DaoException("saveUnitization方法调用异常："+e.getMessage());
        }
        return result;
    }

    @Override
    public Integer clearUnitization() throws DaoException{
        int result;
        try {
            result = this.getSqlSession().insert(getSqlNamespace().concat(".clearUnitization"));
        }catch ( Exception e){
            throw new DaoException("clearUnitization方法调用异常："+e.getMessage());
        }
        return result;
    }

    @Override
    public List<City> getCityByName(String cityName) throws DaoException {
        List<City> result=null;
        try {
            result= this.getSqlSession().selectList(getSqlNamespace()+".getCityByName", cityName);
        }catch (Exception e){
            String msg= SqlSessionMapperLabel.GetObjectById.getName()+":getCityByName"+e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public Integer updateCity(City city) throws DaoException {
        Integer result=0;
        try {
            result= this.getSqlSession().update(getSqlNamespace()+".updateCity", city);
        }catch (Exception e){
            String msg= SqlSessionMapperLabel.UpdateObject.getName()+":updateCity"+e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public MachineRoom getMachineRoomById(String id) throws DaoException {
        MachineRoom result=null;
        try {
            result= this.getSqlSession().selectOne(getSqlNamespace()+".getMachineRoomById", id);
        }catch (Exception e){
            String msg= SqlSessionMapperLabel.GetObjectById.getName()+":getMachineRoomById"+e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public Integer updateMachineRoom(MachineRoom machineRoom) throws DaoException {
        Integer result=0;
        try {
            result= this.getSqlSession().update(getSqlNamespace()+".updateMachineRoom", machineRoom);
        }catch (Exception e){
            String msg= SqlSessionMapperLabel.UpdateObject.getName()+":updateMachineRoom"+e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public Integer delMachineRoomById(String id) throws DaoException {
        Integer result=0;
        try {
            result= this.getSqlSession().delete(getSqlNamespace()+".delMachineRoomById", id);
        }catch (Exception e){
            String msg= SqlSessionMapperLabel.DelObjectById.getName()+":delMachineRoomById"+e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public Machine getMachineById(String id) throws DaoException {
        Machine result=null;
        try {
            result= this.getSqlSession().selectOne(getSqlNamespace()+".getMachineById", id);
        }catch (Exception e){
            String msg= SqlSessionMapperLabel.GetObjectById.getName()+":getMachineById"+e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public Integer updateMachine(Machine machine) throws DaoException {
        Integer result=0;
        try {
            result= this.getSqlSession().update(getSqlNamespace()+".updateMachine", machine);
        }catch (Exception e){
            String msg= SqlSessionMapperLabel.UpdateObject.getName()+":updateMachine"+e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public Integer delMachineById(String id) throws DaoException {
        Integer result=0;
        try {
            result= this.getSqlSession().delete(getSqlNamespace()+".delMachineById", id);
        }catch (Exception e){
            String msg= SqlSessionMapperLabel.DelObjectById.getName()+":delMachineById"+e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public Unitization getUnitizationById(String id) throws DaoException {
        Unitization result=null;
        try {
            result= this.getSqlSession().selectOne(getSqlNamespace()+".getUnitizationById", id);
        }catch (Exception e){
            String msg= SqlSessionMapperLabel.GetObjectById.getName()+":getUnitizationById"+e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public Integer updateUnitization(Unitization unitization) throws DaoException {
        Integer result=0;
        try {
            result= this.getSqlSession().update(getSqlNamespace()+".updateUnitization", unitization);
        }catch (Exception e){
            String msg= SqlSessionMapperLabel.UpdateObject.getName()+":updateUnitization"+e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public Integer delUnitizationById(String id) throws DaoException {
        Integer result=0;
        try {
            result= this.getSqlSession().delete(getSqlNamespace()+".delUnitizationById", id);
        }catch (Exception e){
            String msg= SqlSessionMapperLabel.DelObjectById.getName()+":delUnitizationById"+e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }
}
