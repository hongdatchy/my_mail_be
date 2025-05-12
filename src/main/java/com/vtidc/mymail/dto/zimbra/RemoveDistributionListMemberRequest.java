package com.vtidc.mymail.dto.zimbra;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.*;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "RemoveDistributionListMemberRequest", namespace = "urn:zimbraAdmin")
@NoArgsConstructor
@AllArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class RemoveDistributionListMemberRequest {

    @XmlElement(name = "id")
    private String id;

    @XmlElement(name = "dlm")
    private List<String> dlm;
}
