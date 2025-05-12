package com.vtidc.mymail.repo.projection;

public interface DistributionListProjection extends BaseAuditProjection {

    String getZimbraId();
    Integer getTagId();
    String getDisplayName();
    String getMail();

    Integer getMemberId();
    String getMemberMail();
    String getMemberDisplayName();


}

