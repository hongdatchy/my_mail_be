package com.vtidc.mymail.dto.zimbra;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.*;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "AddDistributionListMemberRequest", namespace = "urn:zimbraAdmin")
@NoArgsConstructor
@AllArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class AddDistributionListMemberRequest {

    @XmlElement(name = "id")
    private String id;

    @XmlElement(name = "dlm", namespace = "urn:zimbraAdmin")
    private List<String> dlm;
}
