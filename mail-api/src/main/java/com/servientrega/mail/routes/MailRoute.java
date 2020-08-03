package com.servientrega.mail.routes;

import static org.apache.camel.language.spel.SpelExpression.spel;

import java.util.ArrayList;

import org.apache.camel.BeanInject;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.language.spel.SpelExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.servientrega.audit.client.notifier.AuditConstants;
import com.servientrega.audit.client.notifier.AuditNotifier;
import com.servientrega.mail.beans.ProcessMailResponseHandler;
import com.servientrega.mail.beans.SuccessResponseHandler;
import com.servientrega.mail.util.Util;

/**
 * 
 * @author Assert Solutions S.A.S
 *
 */
@Component
public class MailRoute extends RouteBuilder {

    private Logger logger = LoggerFactory.getLogger(MailRoute.class);

    @Autowired
    private ProcessMailResponseHandler processMailResponse;

    @Autowired
    private SuccessResponseHandler successResponseHandler;

    @BeanInject("responseJacksonDataFormat")
    private JacksonDataFormat responseDataFormat;
    
    @BeanInject("requestJacksonDataFormat")
    private JacksonDataFormat requestDataFormat;
    
    @Override
    public void configure() throws Exception {
        // @formatter:off
    	from("direct:processMail")
    	    .errorHandler(noErrorHandler())
    	    .streamCaching()
    	    .routeId("ProcessMailRoute")
    	    .to("bean-validator://ValidarRequeridos")
    	    .setProperty("MAIL_MESSAGE", simple("${exchangeProperty.BODY_ORIGINAL.message}"))
            .setProperty("MAIL_PARAMETERS", simple("${exchangeProperty.BODY_ORIGINAL.parameters}"))
            .setProperty("MAIL_RECIPIENTS", simple("${exchangeProperty.BODY_ORIGINAL.recipients}"))
            .setProperty("MAIL_FROM", simple("${exchangeProperty.BODY_ORIGINAL.from}"))
            .setProperty("MAIL_SUBJECT", simple("${exchangeProperty.BODY_ORIGINAL.subject}"))
            .setProperty("MAIL_ISTEMPLATE", simple("${exchangeProperty.BODY_ORIGINAL.isTemplate}"))
            .setProperty("MAIL_ATTACHMENTS", simple("${exchangeProperty.BODY_ORIGINAL.attachments}"))
            .process(exchange -> exchange.setProperty("SEND_MAIL_STATUS", new ArrayList<>()))
            .split(simple("${exchangeProperty.BODY_ORIGINAL.recipients}")) 
                .setProperty("RECIPIENT", body())
                .log(LoggingLevel.INFO, logger, "Recipients: ${body}")
                .doTry()
                    .choice()
                        .when(simple("${exchangeProperty.MAIL_ISTEMPLATE} == true"))
                            .bean(Util.class,"replaceBody")
                     .end()
                     .log(LoggingLevel.INFO, logger,"Enviando email to: ${body.email}")
                     .inOut(SendMailRoute.SEND_MAIL_ENDPOINT)
                     .bean(processMailResponse)
                .endDoTry()
                .doCatch(Exception.class)
                    .setProperty(Exchange.EXCEPTION_HANDLED, constant(true))
                    .log(LoggingLevel.ERROR, logger, "ERROR Sending Mail: ${exception.class}, ${exception.message}: ${exception.stacktrace}")
                    .bean(processMailResponse)
                    // Auditoria
                    .setHeader(AuditConstants.AUDIT_METADATA, simple("${headers}"))
                    .setHeader(AuditConstants.AUDIT_EVENT_TYPE, constant("EXCEPTION_EMAIL_API"))
                    .setHeader(AuditConstants.AUDIT_EVENT_LEVEL, simple(LoggingLevel.ERROR.toString()))
                    .bean(AuditNotifier.class)
                .end()
            .end()
            .bean(successResponseHandler)
            .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
            .marshal(responseDataFormat)
            .log(LoggingLevel.INFO, logger, "Payload Salida: ${body}")
            .unmarshal(responseDataFormat)
    	.end();
    	// @formatter:on
    }

}
