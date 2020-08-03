package com.servientrega.mail;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.servientrega.commons.amq.AMQConfiguration;

/**
 * 
 * @author Assert Solutions
 *
 */
@Configuration
public class TestConfiguration {

    private static final Logger log = LoggerFactory.getLogger(TestConfiguration.class);

    @Bean(name = "amq-component")
    public ActiveMQComponent amqpComponent(AMQConfiguration amqConfiguration) {
        log.info("...............................................................");
        log.info(".. Creating Test AMQ Connection Factory .......................");
        log.info("...............................................................");

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        ActiveMQComponent activeMQComponent = new ActiveMQComponent();
        activeMQComponent.setConnectionFactory(connectionFactory);
        return activeMQComponent;
    }
}
