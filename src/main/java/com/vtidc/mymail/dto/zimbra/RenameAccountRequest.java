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
@XmlRootElement(name = "RenameAccountRequest", namespace = "urn:zimbraAdmin")
@XmlAccessorType(XmlAccessType.FIELD)
public class RenameAccountRequest {

    @XmlElement(name = "id", required = true)
    private String id;

    @XmlElement(name = "newName", namespace = "urn:zimbraAdmin")
    private String newName;
}

