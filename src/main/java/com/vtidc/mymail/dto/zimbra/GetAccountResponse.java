package com.vtidc.mymail.dto.zimbra;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@XmlRootElement(name = "GetAccountResponse", namespace = "urn:zimbraAdmin")
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
@AllArgsConstructor
public class GetAccountResponse {

    @XmlElement(name = "account", namespace = "urn:zimbraAdmin")
    private Account account;

}
