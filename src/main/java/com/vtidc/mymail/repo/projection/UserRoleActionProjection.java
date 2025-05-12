package com.vtidc.mymail.repo.projection;

public interface UserRoleActionProjection extends BaseAuditProjection {

    String getUsername();
    String getMail();
    Integer getRoleId();
    String getRoleName();
    Integer getActionId();
    String getActionName();
    String getConfigData();
    Integer getOrgId();
    String getOrgName();
    String getTagIdList();// json xong parse về list Integer

    Integer getUserSendId();
    String getUserSendOrgName();
    String getUserSendMail();

    String getPassword();

}

