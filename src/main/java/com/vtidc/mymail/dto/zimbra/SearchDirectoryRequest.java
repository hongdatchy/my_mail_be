package com.vtidc.mymail.dto.zimbra;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SearchDirectoryRequest", namespace = "urn:zimbraAdmin")
@AllArgsConstructor
@NoArgsConstructor
public class SearchDirectoryRequest {

    @XmlAttribute
    private int offset;

    @XmlAttribute
    private int limit;

    @XmlAttribute
    private String sortBy;

    @XmlAttribute
    private String sortAscending;

    @XmlAttribute
    private boolean applyCos;

    @XmlAttribute
    private boolean applyConfig;

    @XmlAttribute
    private String attrs;

    @XmlAttribute
    private String types;

    @XmlElement(name = "query", namespace = "urn:zimbraAdmin")
    private String query;
}


