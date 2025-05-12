package com.vtidc.mymail.dto.zimbra;

import jakarta.xml.bind.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;



@XmlRootElement(name = "CreateAccountResponse", namespace = "urn:zimbraAdmin")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountResponse {

    @XmlElement(name = "account", namespace = "urn:zimbraAdmin")
    private Account account;
}

