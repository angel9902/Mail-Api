package com.servientrega.mail.configuration;

import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.servientrega.mail.dto.MailMessage;
import com.servientrega.mail.dto.MailResponse;

@Configuration
public class JacksonConfig {

    @Bean("responseJacksonDataFormat")
    public JacksonDataFormat responseJacksonDataFormat() {
        JacksonDataFormat jacksonDataFormat = new JacksonDataFormat();
        jacksonDataFormat.setPrettyPrint(false);
        jacksonDataFormat.setUnmarshalType(MailResponse.class);
        jacksonDataFormat.setEnableJaxbAnnotationModule(false);
        return jacksonDataFormat;
    }

    @Bean("requestJacksonDataFormat")
    public JacksonDataFormat requestJacksonDataFormat() {
        JacksonDataFormat jacksonDataFormat = new JacksonDataFormat();
        jacksonDataFormat.setPrettyPrint(false);
        jacksonDataFormat.setUnmarshalType(MailMessage.class);
        jacksonDataFormat.setEnableJaxbAnnotationModule(false);
        return jacksonDataFormat;
    }

}
