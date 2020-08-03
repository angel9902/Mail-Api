package com.servientrega.mail.routes;

import org.apache.camel.BeanInject;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.PredicateBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.servientrega.audit.client.notifier.AuditConstants;
import com.servientrega.audit.client.notifier.AuditNotifier;
import com.servientrega.mail.beans.ResponseErrorHandler;
import com.servientrega.mail.exceptions.EmptyMessageException;

/**
 * 
 * @author Assert Solutions S.A.S
 *
 */
@Component
public class EventMailConsumerRoute extends RouteBuilder {

    private Logger logger = LoggerFactory.getLogger(EventMailConsumerRoute.class);

    @BeanInject("requestJacksonDataFormat")
    private JacksonDataFormat requestDataFormat;

    @Autowired
    private ResponseErrorHandler responseHandler;

    @Override
    public void configure() throws Exception {
        // @formatter:off
    	onException(Exception.class)
            .handled(Boolean.TRUE)
            .log(LoggingLevel.ERROR, logger, "${exception.class.name}, ${exception.message}")
            .bean(responseHandler)
            .setHeader(AuditConstants.AUDIT_EVENT_PAYLOAD, exchangeProperty("BODY_ORIGINAL"))
            .setHeader(AuditConstants.AUDIT_EVENT_TYPE, constant("EXCEPTION_EMAIL_API"))
            .setHeader(AuditConstants.AUDIT_EVENT_LEVEL, simple(LoggingLevel.ERROR.toString()))
    	    .bean(AuditNotifier.class)
            .log(LoggingLevel.INFO, logger, "Response Message: ${body}")
        .end();
    	
    	onException(BeanValidationException.class, EmptyMessageException.class)
            .useOriginalMessage()
            .handled(Boolean.TRUE)
            .log(LoggingLevel.ERROR, logger, "${exception.class.name}, ${exception.message}. Body: ${body}")
            .setHeader(AuditConstants.AUDIT_METADATA, simple("${headers}"))
            .setHeader(AuditConstants.AUDIT_EVENT_PAYLOAD, exchangeProperty("BODY_ORIGINAL"))
            .setHeader(AuditConstants.AUDIT_EVENT_TYPE, constant("INVALID_MESSAGE"))
            .setHeader(AuditConstants.AUDIT_EVENT_LEVEL, simple(LoggingLevel.ERROR.toString()))
            .bean(AuditNotifier.class)
            .bean(responseHandler)
            .log(LoggingLevel.INFO, logger, "Response Message: ${body}")
        .end();
        	
    	from("amq:queue:{{email.notification.queue}}?transacted={{email.consumer.transacted}}")
    		.streamCaching()
    	    .routeId("EventMailConsumerRoute")
    	    .log(LoggingLevel.INFO, logger, "Payload Entrada: '${body}'")
    	    .choice()
    	        .when(PredicateBuilder.and(body().isNotNull(), body().isNotEqualTo("")))
        	        .setProperty("PAYLOAD_ORIGINAL", body())
                    .unmarshal(requestDataFormat)
                    .setProperty("BODY_ORIGINAL", body())
                    .to("direct:processMail")
    	        .endChoice()
    	        .otherwise()
    	            .throwException(EmptyMessageException.class, "El mensaje es invalido o nulo")
    	    .end()
    	.end();
    	//@formatter:on
    }

}
