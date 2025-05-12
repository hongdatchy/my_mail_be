package com.vtidc.mymail.dto.zimbra;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "AuthResponse", namespace = "urn:zimbraAdmin")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    @XmlElement(name = "lifetime", namespace = "urn:zimbraAdmin", required = true)
    private Long lifetime;

    @XmlElement(name = "authToken", namespace = "urn:zimbraAdmin", required = true)
    private String authToken;

    @XmlElement(name = "session", namespace = "urn:zimbraAdmin", required = true)
    private Session session;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Session {
        @XmlAttribute(name = "id")
        private String id;

        @XmlAttribute(name = "type")
        private String type;

        @XmlValue
        private String value;
    }
}


