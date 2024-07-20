package com.base.sys.api.entity;

import com.base.api.entity.BaseEntity;

/**
 * @author: liu
 * @date: 2022/9/8 20:04
 * @description:
 */
public class BaseSysAppClientDetail extends BaseEntity {
    //客户端ID client_id
    private String clientId;
    //资源ID集合,多个资源时用逗号(,)分隔 resource_ids
    private String resourceIds;
    //客户端密匙 client_secret
    private String clientSecret;
    //客户端申请的权限范围 scope
    private String scope;
    //客户端支持的grant_type
    private String grantType;
    //重定向URI authorized_grant_types
    private String webServerRedirectUri;
    //客户端所拥有的Spring Security的权限值，多个用逗号(,)分隔 web_server_redirect_uri
    private String authorities;
    //访问令牌有效时间值(单位:秒) access_token_validity
    private String accessTokenValidity;
    //更新令牌有效时间值(单位:秒) refresh_token_validity
    private String refreshTokenValidity;
    //预留字段 additional_information
    private String additionalInformation;
    //用户是否自动Approval操作 autoapprove
    private String autoapprove;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getWebServerRedirectUri() {
        return webServerRedirectUri;
    }

    public void setWebServerRedirectUri(String webServerRedirectUri) {
        this.webServerRedirectUri = webServerRedirectUri;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    public String getAccessTokenValidity() {
        return accessTokenValidity;
    }

    public void setAccessTokenValidity(String accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    public String getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    public void setRefreshTokenValidity(String refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getAutoapprove() {
        return autoapprove;
    }

    public void setAutoapprove(String autoapprove) {
        this.autoapprove = autoapprove;
    }
}
