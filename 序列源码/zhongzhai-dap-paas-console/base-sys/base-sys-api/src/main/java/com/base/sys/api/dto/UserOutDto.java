package com.base.sys.api.dto;

import com.base.sys.api.entity.BaseSysOrg;

import java.util.List;

/**
 * @Author: scalor
 * @Date: 2021/1/15:13:32
 * @Descricption: 返回用户信息，注意所关联的多个角色名和部门名
 */
public class UserOutDto {
    //账号
    private String account;
    //0:男，1：女,3:保密
    private String sex;

    private String birthday;

    private String name;
    //（）
    private String salt;

    private String password;

    private String mail;

    private String phone;
    //0：停用，1：启用
    private String status;
    //0：否，1：是
    private String isDelete;
    //0：否，1：是
    private String isFrozen;

    private String address;

    private String userIp;
    //身份证号
    private String idCast;

    private String userName;

    private String entryDate;
//    关联的部门列表
    private List<BaseSysOrg> orgList ;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getIsFrozen() {
        return isFrozen;
    }

    public void setIsFrozen(String isFrozen) {
        this.isFrozen = isFrozen;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public List<BaseSysOrg> getOrgList() {
        return orgList;
    }

    public void setOrgList(List<BaseSysOrg> orgList) {
        this.orgList = orgList;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getIdCast() {
        return idCast;
    }

    public void setIdCast(String idCast) {
        this.idCast = idCast;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }
}
