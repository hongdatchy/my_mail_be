package com.vtidc.mymail.dto.zimbra;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.*;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RenameDistributionListRequest", namespace = "urn:zimbraAdmin")
@NoArgsConstructor
@AllArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class RenameDistributionListRequest {

    @XmlElement(name = "id")
    private String id;

    @XmlElement(name = "newName")
    private String newName;
}
