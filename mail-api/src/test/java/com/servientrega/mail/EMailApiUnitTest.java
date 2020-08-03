/**
 * 
 */
package com.servientrega.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.apache.activemq.artemis.junit.EmbeddedJMSResource;
import org.apache.camel.BeanInject;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.servientrega.mail.dto.Attachment;
import com.servientrega.mail.dto.MailMessage;
import com.servientrega.mail.dto.MailResponse;
import com.servientrega.mail.dto.Recipient;

/**
 * @author Assert Solutions S.A.S.
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UnitTestConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {
        "server.port=8082" })
public class EMailApiUnitTest {

    @BeanInject
    private CamelContext camelContext;

    private Logger logger = LoggerFactory.getLogger(EMailApiUnitTest.class);

    public static final String SEND_MAIL_ENDPOINT = "direct:SendMailRoute";

    @Rule
    public EmbeddedJMSResource server = new EmbeddedJMSResource(true);

    /**
     * Método para interceptar el envio de emails
     * 
     * @throws Exception
     */
    @Before
    public void interceptorSendMail() throws Exception {
        camelContext.getRouteDefinition("ProcessMailRoute").adviceWith(camelContext, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                mockEndpointsAndSkip("amq*");

                interceptSendToEndpoint(SEND_MAIL_ENDPOINT).skipSendToOriginalEndpoint()
                        .log(LoggingLevel.INFO, logger, "Sending Mail Body: ${body.name} - ${body.email}")
                        .log(LoggingLevel.INFO, logger, "Email was send correct: ${body}");
                // @formatter:off
			}
		});
	}

	/**
	 * Case 1:Método para realizar test del caso donde el mensaje recibido es
	 * valido
	 * @throws Exception
	 */
	@Test
	public void testValidMessage() throws Exception {
		Exchange exchange = sendMessage(Boolean.TRUE, Boolean.TRUE);

		assertNull("Se presentó una exception durante el proceso", exchange.getException());
		assertNotNull(exchange.getIn().getBody());

		MailResponse response = new ObjectMapper().readValue(exchange.getIn().getBody(String.class), MailResponse.class);

		assertNotNull(response.getData());
		assertEquals("Sent successfully", String.valueOf(response.getData().get(0).get("status")));
	}

	/**
	 * Case 3: Método para realizar test del caso donde el mensaje recibido es
	 * invalido
	 * @throws Exception
	 */
    @Test
    public void testInvalidMessage() throws Exception {
    	Exchange exchange = sendMessage(Boolean.FALSE, Boolean.FALSE);
    	assertNotNull(exchange.getProperty(Exchange.EXCEPTION_CAUGHT));
    }
	
    /**
     * Método para enviar peticion a la ruta consumidora de kafka
     * @param validMessage
     * @param useObjectEmail
     * @return
     * @throws Exception
     */
	public Exchange sendMessage(Boolean validMessage, Boolean useObjectEmail) throws Exception {
		ProducerTemplate produce = camelContext.createProducerTemplate();
		Exchange ex = new DefaultExchange(camelContext);

		camelContext.getRouteDefinition("EventMailConsumerRoute").adviceWith(camelContext, new AdviceWithRouteBuilder() {

			@Override
			public void configure() throws Exception {
				replaceFromWith("direct:amqEndpoint");
			}

		});
		if(useObjectEmail) {
			ex.setProperty("BODY_ORIGINAL", buildMessage(validMessage));
			ex.getIn().setBody(new ObjectMapper().writeValueAsString(buildMessage(validMessage)));
		}else {
			ex.setProperty("BODY_ORIGINAL", new MailResponse());
			ex.getIn().setBody( new ObjectMapper().writeValueAsString(new MailResponse()));
		}
		return produce.send("direct:amqEndpoint", ex);
	}

	/**
	 * Método para la construccion del mensaje de entrada.
	 * @param validMessage
	 * @return
	 */
	public MailMessage buildMessage(Boolean validMessage) {
		MailMessage mailMessage = new MailMessage();

		mailMessage.setFrom("openshit.pruebas@servientrega.com");

		mailMessage.setSubject("Test_Email_02-04-2020");

		Recipient recipient = new Recipient();
		recipient.setName("Test Email");
		if (validMessage) {
			recipient.setEmail("test@servientrega.com");
		}
		List<Recipient> listRecipient = new ArrayList<>();
		listRecipient.add(recipient);

		mailMessage.setRecipients(listRecipient);
		mailMessage.setMessage("This is a unit Test");
		mailMessage.setIsTemplate(Boolean.FALSE);

		List<Attachment> lisAttachments = new ArrayList<Attachment>();
		mailMessage.setAttachments(lisAttachments);
		return mailMessage;
	}
}
