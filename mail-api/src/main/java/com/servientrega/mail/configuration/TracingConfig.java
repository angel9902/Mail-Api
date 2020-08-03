package com.servientrega.mail.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.servientrega.commons.eventnotifier.RequestIdEventNotifier;

/**
 * 
 * @author Assert Solutions
 *
 */
@Configuration
public class TracingConfig {

    @Bean
    public RequestIdEventNotifier eventNotifier() {
        return new RequestIdEventNotifier();
    }
}
