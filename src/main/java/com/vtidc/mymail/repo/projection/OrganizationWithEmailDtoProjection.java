package com.vtidc.mymail.repo.projection;

import java.time.LocalDate;

public interface OrganizationWithEmailDtoProjection {
    // Từ bảng organization
    Integer getId();
    String getName();
    String getMail();
    String getStatus();
    String getTradeName();
    String getCompanyType();
    String getTaxCode();
    String getAddress();
    String getLegalRepresentative();
    LocalDate getLicenseIssuedDate();
    LocalDate getOperationStartDate();
    String getPhoneNumber();
    String getLicenseFile();

    // Từ bảng email
    Integer getEmailId();
    String getZimbraId();
    String getDisplayName();
    String getEmailMail();
    String getType();
}
