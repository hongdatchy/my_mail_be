package com.vtidc.mymail.dto;

import com.vtidc.mymail.entities.auditing.AbstractAuditingEntity;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveOrganizationDto extends AbstractAuditingEntity {
    private Integer id;

    @Size(max = 120)
    private String name;

    @Size(max = 120)
    private String status;

    @Size(max = 255)
    private String tradeName;

    @Size(max = 255)
    private String companyType;

    @Size(max = 20)
    private String taxCode;

    @Size(max = 500)
    private String address;

    @Size(max = 255)
    private String legalRepresentative;

    private LocalDate licenseIssuedDate;

    private LocalDate operationStartDate;

    @Size(max = 20)
    private String phoneNumber;

    @Size(max = 255)
    private String licenseFile;

    private List<Integer> tagIdList;

}