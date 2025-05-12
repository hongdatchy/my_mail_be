package com.vtidc.mymail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SaveRoleDto {

    private Integer roleId;

    private String roleName;

    private Integer actionId;

    private String actionName;

    private Map<String, Object> configData;

}
