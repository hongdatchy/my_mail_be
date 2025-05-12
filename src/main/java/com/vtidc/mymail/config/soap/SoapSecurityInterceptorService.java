package com.vtidc.mymail.config.soap;

import com.vtidc.mymail.dto.zimbra.AuthRequest;
import com.vtidc.mymail.dto.zimbra.AuthResponse;
import lombok.Setter;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;
import org.springframework.xml.transform.StringSource;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

public class SoapSecurityInterceptorService implements ClientInterceptor {

    private final TokenMemory tokenMemory;

    private final Jaxb2Marshaller marshaller;

    private final SoapRemoveHeadersInterceptor soapRemoveHeadersInterceptor;


    @Setter
    private String account = null;

    public SoapSecurityInterceptorService(Jaxb2Marshaller marshaller, SoapRemoveHeadersInterceptor soapRemoveHeadersInterceptor, TokenMemory tokenMemory

    ) {
        this.marshaller = marshaller;
        this.soapRemoveHeadersInterceptor = soapRemoveHeadersInterceptor;
        this.tokenMemory = tokenMemory;
    }

    private static final String ZIMBRA_ADMIN_URL = "https://mail.example.com:7071/service/admin/soap";

    private AuthResponse authenticateAndGetAccount(AuthRequest authRequest) {
        try {
            HttpClient client = HttpClients.custom().addInterceptorFirst(soapRemoveHeadersInterceptor).setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build()).build();

            SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
            messageFactory.afterPropertiesSet();
            messageFactory.afterPropertiesSet();

            WebServiceTemplate webServiceTemplate = new WebServiceTemplate(messageFactory);
            webServiceTemplate.setMarshaller(marshaller);
            webServiceTemplate.setUnmarshaller(marshaller);
            HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender(client);

            webServiceTemplate.setMessageSender(messageSender);
            return (AuthResponse) webServiceTemplate.marshalSendAndReceive(ZIMBRA_ADMIN_URL, authRequest);
        } catch (Exception ignored) {
            return null;
        }
    }

    private AuthResponse authenticateAndGetAccount2(AuthRequest authRequest) {
        try {
            HttpClient client = HttpClients.custom().addInterceptorFirst(soapRemoveHeadersInterceptor).setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build()).build();

            SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
            messageFactory.afterPropertiesSet();

            WebServiceTemplate webServiceTemplate = new WebServiceTemplate(messageFactory);
            webServiceTemplate.setMarshaller(marshaller);
            webServiceTemplate.setUnmarshaller(marshaller);
            webServiceTemplate.setMessageSender(new HttpComponentsMessageSender(client));

            return (AuthResponse) webServiceTemplate.marshalSendAndReceive(ZIMBRA_ADMIN_URL, authRequest, message -> {
                SoapMessage soapMessage = (SoapMessage) message;
                String headerXml = """
                            <context xmlns="urn:zimbra">
                                <session id="0"/>
                                <userAgent name="MyApp"/>
                            </context>
                        """;
                try {
                    Transformer transformer;
                    StringSource headerSource = new StringSource(headerXml);
                    transformer = TransformerFactory.newInstance().newTransformer();
                    transformer.transform(headerSource, soapMessage.getSoapHeader().getResult());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
        SoapMessage soapMessage = (SoapMessage) messageContext.getRequest();


        if (tokenMemory.isTokenExpired()) {
            AuthResponse authResponse = authenticateAndGetAccount2(new AuthRequest("admin", "hongdat10"));
            if (authResponse == null) {
                return false;
            }
            tokenMemory.saveToken(authResponse);
        }
        String token = tokenMemory.getToken();
        String sessionId = tokenMemory.getSessionId();

        StringBuilder headerXmlBuilder = new StringBuilder("""
                    <context xmlns="urn:zimbra">
                        <session id="%s"/>
                        <authToken>%s</authToken>
                """.formatted(sessionId, token));


        if (account != null) {
            headerXmlBuilder.append("<account by=\"name\">").append(account).append("</account>\n");
        }

        headerXmlBuilder.append("</context>");

        String headerXml = headerXmlBuilder.toString();


        try {
            Transformer transformer;
            StringSource headerSource = new StringSource(headerXml);
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(headerSource, soapMessage.getSoapHeader().getResult());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
        return false;
    }

    @Override
    public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
        return false;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Exception e) throws WebServiceClientException {

    }
}
