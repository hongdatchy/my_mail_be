package com.vtidc.mymail.config.soap;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@AllArgsConstructor
public class SoapSecurityInterceptorServiceConfig {

    private final Jaxb2Marshaller jaxb2Marshaller;

    private SoapRemoveHeadersInterceptor soapRemoveHeadersInterceptor;

    private TokenMemory tokenMemory;

    @Bean
    @Qualifier("admin")
    public SoapSecurityInterceptorService soapSecurityInterceptorServiceAdmin() {
        return new SoapSecurityInterceptorService(jaxb2Marshaller, soapRemoveHeadersInterceptor,
                tokenMemory);
    }

    @Bean
    @Qualifier("account")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SoapSecurityInterceptorService soapSecurityInterceptorServiceAccount() {
        return new SoapSecurityInterceptorService(jaxb2Marshaller, soapRemoveHeadersInterceptor,
                tokenMemory);
    }
}