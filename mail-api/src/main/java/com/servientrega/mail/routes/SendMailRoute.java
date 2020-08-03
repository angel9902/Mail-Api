package com.servientrega.mail.routes;

import org.apache.camel.BeanInject;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mail.MailEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.servientrega.mail.util.AttachmentUtil;

/**
 * 
 * @author Assert Solutions S.A.S
 *
 */
@Component
public class SendMailRoute extends RouteBuilder {

    public static final String SEND_MAIL_ENDPOINT = "direct:SendMailRoute";
    private Logger logger = LoggerFactory.getLogger(SendMailRoute.class);

    @BeanInject("smtpEndpoint")
    private MailEndpoint smtpEndpoint;

    @Override
    public void configure() throws Exception {
        // @formatter:off
        from(SEND_MAIL_ENDPOINT).streamCaching()
            .routeId("SendMailRoute")
            .errorHandler(noErrorHandler())
            .removeHeaders("*")
            .setHeader("to", simple("${body.email}"))
            .setHeader("from", exchangeProperty("MAIL_FROM"))
            .setHeader("subject", exchangeProperty("MAIL_SUBJECT"))
            .choice()
                .when(exchangeProperty("MAIL_ATTACHMENTS").isNotNull())
                    .bean(AttachmentUtil.class)
                .endChoice()
             .end()
             .setBody(exchangeProperty("MAIL_MESSAGE"))
             .log(LoggingLevel.DEBUG, logger,"Sending Mail Body: ${body}")
             .loadBalance().roundRobin()
                 // EndPoint SMTP1
                 .to(smtpEndpoint)
             .end()
             .log(LoggingLevel.INFO,logger, "E-mail successfully sent to: ${headers.to}")
        .end();
        // @formatter:on
    }

}
