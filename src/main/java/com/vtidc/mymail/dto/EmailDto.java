package com.vtidc.mymail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailDto {

    private Integer id;
    private String mail;
    private String displayName;

}
