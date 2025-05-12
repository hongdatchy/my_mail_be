package com.vtidc.mymail.dto.zimbra;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.*;

@Data
@XmlRootElement(name = "ChangePasswordResponse", namespace = "urn:zimbraAccount")
@XmlAccessorType(XmlAccessType.FIELD)
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordResponse {

    @XmlElement(name = "authToken")
    private String authToken;

    @XmlElement(name = "lifetime")
    private Long lifetime;
}

