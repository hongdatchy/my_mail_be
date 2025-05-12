package com.vtidc.mymail.dto.zimbra;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@XmlRootElement(name = "CreateDistributionListRequest", namespace = "urn:zimbraAdmin")
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
@AllArgsConstructor
public class CreateDistributionListRequest {

    @XmlAttribute(name = "name", required = true)
    private String name;

    @XmlElement(name = "a")
    private List<Attr> attrs;
}
