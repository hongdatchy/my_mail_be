package com.vtidc.mymail.repo.projection;

import java.time.Instant;

public interface FlowEmailProjection extends BaseAuditProjection {

    String getName();
    String getStatus();
    Integer getTagId();
    Instant getStartDate();
    Byte getStartNow();
    String getContent();

    Integer getFlowEmailFromEntityId();
    String getFlowEmailFromType();
    String getFlowEmailFromEmail();

    Integer getFlowEmailToEntityId();
    String getFlowEmailToType();
    String getFlowEmailToEmail();

    Integer getFlowEmailApproveEntityId();
    String getFlowEmailApproveType();
    String getFlowEmailApproveEmail();


}


