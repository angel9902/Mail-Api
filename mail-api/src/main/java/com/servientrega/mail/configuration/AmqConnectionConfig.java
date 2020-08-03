package com.servientrega.mail.configuration;

import java.util.Objects;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.servientrega.commons.amq.AMQConfiguration;

/**
 * Clase para configurar el componente amqp de camel para el procesamiento de
 * mensajes con AMQ Broker
 * 
 * @author Assert Solutions S.A.S
 *
 */
@Configuration
public class AmqConnectionConfig {

    private static final Logger log = LoggerFactory.getLogger(AmqConnectionConfig.class);

    @SuppressWarnings("unused")
    private Logger logger = LoggerFactory.getLogger(AmqConnectionConfig.class);

    @Bean(name = "amq-component")
    public ActiveMQComponent amqpComponent(AMQConfiguration amqConfiguration) {
        log.info("...............................................................");
        log.info(".. Creating AMQ Connection Factory ............................");
        log.info("...............................................................");
        String url = "tcp://" + amqConfiguration.getHost() + ":" + amqConfiguration.getPort();
        log.info("AMQ Broker URL: {}", url);

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        if (!Objects.isNull(amqConfiguration.getUsername()) && !Objects.isNull(amqConfiguration.getPassword())) {
            connectionFactory.setUser(amqConfiguration.getUsername());
            connectionFactory.setPassword(amqConfiguration.getPassword());
        }
        ActiveMQComponent activeMQComponent = new ActiveMQComponent();
        activeMQComponent.setConnectionFactory(connectionFactory);
        return activeMQComponent;
    }
}
