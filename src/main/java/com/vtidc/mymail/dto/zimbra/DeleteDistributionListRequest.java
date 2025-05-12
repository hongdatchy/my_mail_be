package com.vtidc.mymail.dto.zimbra;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "DeleteDistributionListRequest", namespace = "urn:zimbraAdmin")
public class DeleteDistributionListRequest {

    @XmlAttribute(name = "id", required = true)
    private String id;

}

