package com.base.sys.api.dto;

import com.base.sys.api.entity.BaseSysApp;
import com.base.sys.api.entity.BaseSysOrg;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class AuthenticationUserDto implements Serializable {
    private static final long serialVersionUID = -8468376740538529560L;
    //账号
    private String account;
    //0:男，1：女,3:保密
    private String sex;

    private String birthday;

    private String name;

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


    private String version;

    private String entryDate;
    //关联的部门列表
    private List<BaseSysOrg> orgList ;
    //用户拥有的地址
    private List<String>  urls;
    //系统注册的地址
    private List<String>  allUrls;

    //用户id
    private String userId;
    private String tenantId;
    private String tenantCode;
    private String authorization;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * 菜单列表
     */
    private List<SysMenuDto>  allSysMenuDtoList;
    /**
     * 拥有的菜单
     */
    private List<SysMenuDto>  sysMenuDtoList;

    /**
     * 当前用户的请求请求查询地址->请求规则
     */
    private Map<String,String> dataPermessionList;

    private  String sessionId;

    /**
     * 当前用户下的应用
     */
    private List<BaseSysApp>  appList;

    /**
     * 控制按钮隐藏
     */
    private transient List<SysOperateSign>  sysOperateSignList;



    private  Map<String,Object> userAppMenuMap;



    public Map<String, Object> getUserAppMenuMap() {
        return userAppMenuMap;
    }

    public void setUserAppMenuMap(Map<String, Object> userAppMenuMap) {
        this.userAppMenuMap = userAppMenuMap;
    }

    public List<BaseSysApp> getAppList() {
        return appList;
    }

    public void setAppList(List<BaseSysApp> appList) {
        this.appList = appList;
    }

    public List<SysMenuDto> getAllSysMenuDtoList() {
        return allSysMenuDtoList;
    }

    public void setAllSysMenuDtoList(List<SysMenuDto> allSysMenuDtoList) {
        this.allSysMenuDtoList = allSysMenuDtoList;
    }

    public List<SysMenuDto> getSysMenuDtoList() {
        return sysMenuDtoList;
    }

    public void setSysMenuDtoList(List<SysMenuDto> sysMenuDtoList) {
        this.sysMenuDtoList = sysMenuDtoList;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

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

    public List<BaseSysOrg> getOrgList() {
        return orgList;
    }

    public void setOrgList(List<BaseSysOrg> orgList) {
        this.orgList = orgList;
    }


    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getAllUrls() {
        return allUrls;
    }

    public void setAllUrls(List<String> allUrls) {
        this.allUrls = allUrls;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, String> getDataPermessionList() {
        return dataPermessionList;
    }

    public void setDataPermessionList(Map<String, String> dataPermessionList) {
        this.dataPermessionList = dataPermessionList;
    }

    public List<SysOperateSign> getSysOperateSignList() {
        return sysOperateSignList;
    }

    public void setSysOperateSignList(List<SysOperateSign> sysOperateSignList) {
        this.sysOperateSignList = sysOperateSignList;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }
}
