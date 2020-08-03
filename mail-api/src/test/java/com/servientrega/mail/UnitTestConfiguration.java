package com.servientrega.mail;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.servientrega.audit.client.notifier.AuditNotifier;
import com.servientrega.commons.amq.AMQConfiguration;

/**
 * 
 * @author Assert Solutions
 *
 */
@TestConfiguration
public class UnitTestConfiguration {

    private static final Logger log = LoggerFactory.getLogger(UnitTestConfiguration.class);

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

    @Bean(name = "auditNotifier")
    public AuditNotifier auditNotifier() {
        return Mockito.mock(AuditNotifier.class);
    }
}
