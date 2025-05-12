package com.vtidc.mymail.dto.zimbra;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "CreateAccountRequest", namespace = "urn:zimbraAdmin")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateAccountRequest {

    @XmlElement(name = "name", namespace = "urn:zimbraAdmin")
    private String name;

    @XmlElement(name = "password", namespace = "urn:zimbraAdmin")
    private String password;

    @XmlElement(name = "a")
    private List<Attr> attrs;
}

