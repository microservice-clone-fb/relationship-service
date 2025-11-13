package com.tam.relationship.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tam.relationship.service.AuditService;
import com.tam.relationship.utils.AuditListener;

@Configuration
public class AuditConfiguration {

    @Bean
    @ConditionalOnMissingBean(AuditService.class)
    public AuditService auditService() {
        return new AuditService();
    }

    @Bean
    @ConditionalOnMissingBean(AuditListener.class)
    public AuditListener auditEntityListener(AuditService auditService) {
        AuditListener listener = new AuditListener();
        listener.setAuditService(auditService);
        return listener;
    }
}
