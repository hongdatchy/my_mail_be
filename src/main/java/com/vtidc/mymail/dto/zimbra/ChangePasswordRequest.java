package com.vtidc.mymail.dto.zimbra;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.*;

@Data
@XmlRootElement(name = "ChangePasswordRequest", namespace = "urn:zimbraAccount")
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {

    @XmlElement(name = "account", namespace = "urn:zimbraAccount", required = true)
    private AccountSelector account;

    @XmlElement(name = "oldPassword", required = true)
    private String oldPassword;

    @XmlElement(name = "password", required = true)
    private String password;


}
