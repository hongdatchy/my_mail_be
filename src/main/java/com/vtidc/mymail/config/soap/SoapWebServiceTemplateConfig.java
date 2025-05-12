package com.vtidc.mymail.config.soap;

import jakarta.xml.soap.SOAPException;
import lombok.AllArgsConstructor;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Configuration
@AllArgsConstructor
public class SoapWebServiceTemplateConfig {


    private final Jaxb2Marshaller jaxb2Marshaller;

    private SoapRemoveHeadersInterceptor soapRemoveHeadersInterceptor;

    private SoapLoggerInterceptor soapLoggerInterceptor;

    private final SoapSecurityInterceptorService soapSecurityInterceptorServiceAdmin;

    @Bean
    @Qualifier("admin")
    public WebServiceTemplate webServiceTemplate() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, SOAPException {
        HttpClient client = HttpClients.custom()
                .addInterceptorFirst(soapRemoveHeadersInterceptor)
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .setSSLContext(new SSLContextBuilder()
                        .loadTrustMaterial(null, (chain, authType) -> true)
                        .build())
                .build();

        SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
        messageFactory.afterPropertiesSet();
        WebServiceTemplate webServiceTemplate = new WebServiceTemplate(messageFactory);
        webServiceTemplate.setMarshaller(jaxb2Marshaller);
        webServiceTemplate.setUnmarshaller(jaxb2Marshaller);
        webServiceTemplate.setInterceptors(new ClientInterceptor[]{
                soapSecurityInterceptorServiceAdmin,
                soapLoggerInterceptor,
        });
        // Tạo HTTP client để thêm headers (Cookie và Content-Type)
        HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender(client);

        webServiceTemplate.setMessageSender(messageSender);
        return webServiceTemplate;
    }

    @Bean
    @Qualifier("account")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public WebServiceTemplate webServiceTemplateAccount() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, SOAPException {
        HttpClient client = HttpClients.custom()
                .addInterceptorFirst(soapRemoveHeadersInterceptor)
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .setSSLContext(new SSLContextBuilder()
                        .loadTrustMaterial(null, (chain, authType) -> true)
                        .build())
                .build();

        SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
        messageFactory.afterPropertiesSet();
        messageFactory.afterPropertiesSet();

        WebServiceTemplate webServiceTemplate = new WebServiceTemplate(messageFactory);
        webServiceTemplate.setMarshaller(jaxb2Marshaller);
        webServiceTemplate.setUnmarshaller(jaxb2Marshaller);
        // Tạo HTTP client để thêm headers (Cookie và Content-Type)
        HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender(client);

        webServiceTemplate.setMessageSender(messageSender);
        return webServiceTemplate;
    }


}
