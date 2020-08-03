package com.servientrega.mail.routes;

import javax.ws.rs.core.MediaType;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.servientrega.audit.client.notifier.AuditConstants;
import com.servientrega.audit.client.notifier.AuditNotifier;
import com.servientrega.mail.beans.ResponseErrorHandler;
import com.servientrega.mail.dto.MailMessage;
import com.servientrega.mail.dto.MailResponse;

/**
 * 
 * @author Assert Solutions S.A.S
 *
 */
@Component
public class EMailApiRestDslRoute extends RouteBuilder {

    @Autowired
    private ResponseErrorHandler responseHandler;

    private Logger logger = LoggerFactory.getLogger(EMailApiRestDslRoute.class);

    public static final String MAIN_ROUTE_ENDPOINT = "direct:MailApiRoute";

    @Override
    public void configure() throws Exception {
        // @formatter:off
    	onException(Exception.class)
    	    .useOriginalMessage()
            .handled(Boolean.TRUE)
            .log(LoggingLevel.ERROR, logger, "${exception.class.name}, ${exception.message}. Body: ${body}")
            .setHeader(AuditConstants.AUDIT_EVENT_PAYLOAD, exchangeProperty("BODY_ORIGINAL"))
            .setHeader(AuditConstants.AUDIT_EVENT_TYPE, constant("EXCEPTION_EMAIL_API"))
            .setHeader(AuditConstants.AUDIT_EVENT_LEVEL, simple(LoggingLevel.ERROR.toString()))
            .bean(AuditNotifier.class)
            .bean(responseHandler)
            .log(LoggingLevel.INFO, logger, "Response Message: ${body}")
        .end();
    	
    	onException(BeanValidationException.class)
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
    	
        rest("{{email.api.rest.path}}")
            .consumes(MediaType.APPLICATION_JSON)
            .produces(MediaType.APPLICATION_JSON)
        .post()
            .type(MailMessage.class)
            .outType(MailResponse.class)
            .to(MAIN_ROUTE_ENDPOINT);

        from(MAIN_ROUTE_ENDPOINT)
			.streamCaching()
            .routeId("MailApiRoute")
            .setProperty("BODY_ORIGINAL", body())
            .log(LoggingLevel.INFO, logger, "Payload Entrada: ${body}")
            .setProperty("PAYLOAD_ORIGINAL", body())
            .to("direct:processMail")
        .end();
        // @formatter:on
    }

}
