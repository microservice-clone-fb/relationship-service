package com.tam.relationship.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tam.relationship.service.AuditServices;
import com.tam.relationship.utils.AuditListener;

// @Deprecated
@Configuration
public class AuditConfiguration {

    @Bean
    @ConditionalOnMissingBean(AuditServices.class)
    public AuditServices auditService() {
        return new AuditServices();
    }

    @Bean
    @ConditionalOnMissingBean(AuditListener.class)
    public AuditListener auditEntityListener() {
        return new AuditListener();
    }
}
