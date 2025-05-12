package com.vtidc.mymail.dto.zimbra;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @XmlElement(name = "e")
    private List<EmailAddress> emailAddresses;

    @XmlElement(name = "su")
    private String subject;

    @XmlElement(name = "mp")
    private MimePart mimePart;
}
