/**
 * 
 */
package com.exercise.masters.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author michaeldelacruz
 *
 */

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {

	@Bean
    public AuditorAware<String> auditorProvider(){
        return () -> Optional.ofNullable("exer3");
    }
	
}
