package com.base.sys.api.entity;


import com.base.api.entity.BaseEntity;

import java.util.Date;

/**
 * 用户表(BaseSysUser)表实体类
 *
 * @author makejava
 * @since 2021-01-14 09:32:48
 */
public class BaseSysUser extends BaseEntity {
   //账号
    private String account;
    //0:男，1：女,3:保密
    private String sex;

    private String birthday;

    private String name;
//（）
    private String salt;

    private String password;

    private String email;

    private String phone;
    //0：停用，1：启用
    private String status;
    //0：否，1：是
    private String isDelete;

    private String address;

    private String userIp;
    //身份证号
    private String idCast;

    //昵称
    private String nickName;

    private String entryDate;

    private String companyId;
//    公司名
    private String companyOrgName;

    private String orgId;
//    部门名
    private String orgName;
//    添加roleId 在关联查询中 用到，勿动
    private String roleId;

    //0：否，1：是
    private String isLock;
    //错误次数
    private Integer errorCount;
    //密码最后一次输入错误时间
    private Date lastErrorDate;
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getCompanyOrgName() {
        return companyOrgName;
    }

    public void setCompanyOrgName(String companyOrgName) {
        this.companyOrgName = companyOrgName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getPassword()

    {
        return password;
    }

    public void setPassword(String password) { this.password =password; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getIsLock() {
        return isLock;
    }

    public void setIsLock(String isLock) {
        this.isLock = isLock;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public Date getLastErrorDate() {
        return lastErrorDate;
    }

    public void setLastErrorDate(Date lastErrorDate) {
        this.lastErrorDate = lastErrorDate;
    }
}
