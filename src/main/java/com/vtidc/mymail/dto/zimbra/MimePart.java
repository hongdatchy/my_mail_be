package com.vtidc.mymail.dto.zimbra;

import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.xml.bind.annotation.*;
import lombok.NoArgsConstructor;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
@AllArgsConstructor
public class MimePart {

    @XmlAttribute(name = "ct")
    private String contentType;

    @XmlElement(name = "content")
    private String content;
}
