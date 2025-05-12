package com.vtidc.mymail.dto.zimbra;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

import java.util.List;

@Data
@XmlRootElement(name = "CreateDistributionListResponse", namespace = "urn:zimbraAdmin")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateDistributionListResponse {

    @XmlElement(name = "dl", namespace = "urn:zimbraAdmin")
    private DistributionListInfo dl;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class DistributionListInfo {

        @XmlAttribute(name = "dynamic")
        private Boolean dynamic;

        @XmlAttribute(name = "name")
        private String name;

        @XmlAttribute(name = "id")
        private String id;

        @XmlElement(name = "dlm")
        private List<String> members;

        @XmlElementWrapper(name = "owners")
        @XmlElement(name = "owner")
        private List<GranteeInfo> owners;

        @XmlElement(name = "a")
        private List<Attr> attrs;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class GranteeInfo {
        @XmlAttribute(name = "type")
        private String type;

        @XmlAttribute(name = "id")
        private String id;

        @XmlAttribute(name = "name")
        private String name;
    }
}
