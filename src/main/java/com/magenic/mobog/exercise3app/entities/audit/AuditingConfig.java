package com.magenic.mobog.exercise3app.entities.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditingConfig {
    private static final String AUDIT_PROVIDER = "mobog";
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of(AUDIT_PROVIDER);
    }
}
