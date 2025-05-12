package com.vtidc.mymail.dto.zimbra;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @XmlAttribute(name = "isExternal")
    private boolean isExternal;

    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "id")
    private String id;

    private String displayName;

    @XmlElement(name = "a", namespace = "urn:zimbraAdmin")
    private List<Attr> attrs;
}