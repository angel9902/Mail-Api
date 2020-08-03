package com.servientrega.mail.configuration;

import java.util.Properties;

import javax.ws.rs.core.MediaType;

import org.apache.camel.CamelContext;
import org.apache.camel.component.mail.MailConfiguration;
import org.apache.camel.component.mail.MailEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 
 * @author Assert Solutions
 *
 */
@Configuration
public class SmtpConfig {

    private static final String SMTP_ENABLE_SSL = "mail.smtp.starttls.enable";
    @Autowired
    private Environment env;
    @Autowired
    private CamelContext camelContext;
    @Autowired
    private Properties props;

    @Bean(name = "smtpEndpoint")
    public MailEndpoint mailEndpoint() {
        props.put(SMTP_ENABLE_SSL, "true");
        MailEndpoint mailEndpoint = new MailEndpoint();
        MailConfiguration mailConfiguration = new MailConfiguration();
        mailConfiguration.setHost(env.getProperty("smtp.server.url"));
        mailConfiguration.setUsername(env.getProperty("smtp.server.username"));
        mailConfiguration.setPassword(env.getProperty("smtp.server.password"));
        mailConfiguration.setPort(Integer.valueOf(env.getProperty("smtp.server.port")));
        mailConfiguration.setProtocol("smtp");
        mailConfiguration.setContentType(MediaType.TEXT_HTML);
        mailConfiguration.setAdditionalJavaMailProperties(props);
        mailEndpoint.setConfiguration(mailConfiguration);
        mailEndpoint.setCamelContext(camelContext);
        return mailEndpoint;
    }

}
