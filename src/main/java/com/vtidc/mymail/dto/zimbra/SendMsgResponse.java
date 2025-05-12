package com.vtidc.mymail.dto.zimbra;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "SendMsgResponse", namespace = "urn:zimbraMail")
@XmlAccessorType(XmlAccessType.FIELD)
public class SendMsgResponse {

    @XmlElement(name = "m", namespace = "urn:zimbraMail")
    private MessageInfo message;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class MessageInfo {
        @XmlAttribute(name = "id")
        private String id;
    }
}
