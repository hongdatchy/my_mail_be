package com.vtidc.mymail.dto.zimbra;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "DeleteAccountRequest", namespace = "urn:zimbraAdmin")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteAccountRequest {

    @XmlAttribute(name = "id", required = true)
    private String id;

}

