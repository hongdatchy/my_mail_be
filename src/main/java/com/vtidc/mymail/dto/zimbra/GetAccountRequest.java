package com.vtidc.mymail.dto.zimbra;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "GetAccountRequest", namespace = "urn:zimbraAdmin")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetAccountRequest {

    @XmlElement(name = "account", namespace = "urn:zimbraAdmin")
    private AccountRequest account;

    @XmlAttribute
    private String attrs;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class AccountRequest {
        @XmlAttribute(name = "by")
        private String by;

        @XmlValue
        private String value;
    }
}
