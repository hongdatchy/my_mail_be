package com.vtidc.mymail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SaveUserApproveEmailDto {

    private Integer userApproveId;

    private Integer userSendId;

    private String userSendOrgName;

    private String userSendMail;

}
