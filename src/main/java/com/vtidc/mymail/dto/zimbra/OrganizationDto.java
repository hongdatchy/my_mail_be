package com.vtidc.mymail.dto.zimbra;

import com.vtidc.mymail.dto.EmailDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDto {
    private Integer id;
    private String name;
    private String mail;
    private String status;
    private String tradeName;
    private String companyType;
    private String taxCode;
    private String address;
    private String legalRepresentative;
    private LocalDate licenseIssuedDate;
    private LocalDate operationStartDate;
    private String phoneNumber;
    private String licenseFile;
    private EmailDto email;
}
