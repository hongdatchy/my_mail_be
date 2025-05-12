package com.vtidc.mymail.repo.projection;

import java.time.Instant;

public interface BaseAuditProjection {
    Integer getId();
    String getCreatedBy();
    Instant getCreatedDate();
    String getUpdatedBy();
    Instant getUpdatedDate();
}

