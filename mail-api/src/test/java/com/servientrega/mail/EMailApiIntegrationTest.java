package com.servientrega.mail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.servientrega.mail.dto.Attachment;
import com.servientrega.mail.dto.MailMessage;
import com.servientrega.mail.dto.MailResponse;
import com.servientrega.mail.dto.Recipient;

/**
 * 
 * @author Assert Solutions S.A.S
 *
 */
@RunWith(SpringRunner.class)
@Configuration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = { "server.port=8082" })
public class EMailApiIntegrationTest {

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private TestRestTemplate restTemplate;
    private Logger logger = LoggerFactory.getLogger(EMailApiIntegrationTest.class);

    @Test
    public void testValidationFieldsComplete() throws Exception {

        MailMessage mailMessage = new MailMessage();
        mailMessage.setFrom("openshit.pruebas@servientrega.com");
		mailMessage.setSubject("TestEmailSend");
        Recipient recipient = new Recipient();
        recipient.setName("Juan");
		recipient.setEmail("danielesv1995@gmail.com");
//        recipient.setCc("jakspok@gmail.com");
//        recipient.setBcc("jakspok@gmail.com");
        List<Recipient> listRecipient = new ArrayList<>();
        listRecipient.add(recipient);

        mailMessage.setRecipients(listRecipient);
        mailMessage.setMessage("Test Correo${MAIL_MESSAGE}");
        mailMessage.setIsTemplate(true);

        Attachment attachment = new Attachment();
        attachment.setName("MensajedePrueba.txt");
        attachment.setValue("RGViZSBpciBlbCBjb250ZW5pZG8gZGVsIGFyY2hpdm8gY29kaWZpY2FkbyBlbiBiYXNlIDY0");

        List<Attachment> lisAttachments = new ArrayList<Attachment>();
        lisAttachments.add(attachment);
        mailMessage.setAttachments(lisAttachments);

        ResponseEntity<MailResponse> response = restTemplate.postForEntity(
                "http://localhost:" + serverPort + "/api/messaging/mails", mailMessage, MailResponse.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertThat(response.getBody().getData().get(0).get("status").equals("Sent successfully"));

        logger.info("Estado : {}", response.getStatusCode());
        logger.info("Respuesta Status : {}", response.getBody().getData().get(0).get("status"));

    }

    @Test
    public void testValidationFieldsInComplete() throws Exception {

        MailMessage mailMessage = new MailMessage();
        mailMessage.setMessage("Test Correo${MAIL_MESSAGE}");
        Attachment attachment = new Attachment();
        attachment.setName("Documentos.pdf");
        attachment.setValue("Saludo");
        List<Attachment> lisAttachments = new ArrayList<Attachment>();
        lisAttachments.add(attachment);
        mailMessage.setAttachments(lisAttachments);

        ResponseEntity<MailResponse> response = restTemplate.postForEntity(
                "http://localhost:" + serverPort + "/api/messaging/mails", mailMessage, MailResponse.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertThat(response.getBody().getErrors().get(0).getError().equals("Validation Error"));

        logger.info("Estado : {}", response.getStatusCode());
        logger.info("Error  : {}", response.getBody().getErrors().get(0).getError());
        logger.info("Descripcion   : {}", response.getBody().getErrors().get(0).getDescription());

    }

//    @Test
    public void testValidationFieldsRequired() throws Exception {

        MailMessage mailMessage = new MailMessage();
        mailMessage.setFrom("openshit.pruebas@servientrega.com");
        mailMessage.setSubject("Test");
        mailMessage.setMessage("Test Correo${MAIL_MESSAGE}");
        mailMessage.setIsTemplate(Boolean.TRUE);

        Recipient recipient = new Recipient();
        recipient.setEmail("prueba@test.com");

        Attachment attachment = new Attachment();
        attachment.setName("Documentos.zip");
        attachment.setValue("sakldjaidsa890da0ud");

        ResponseEntity<MailResponse> response = restTemplate.postForEntity(
                "http://localhost:" + serverPort + "/api/messaging/mails", mailMessage, MailResponse.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertThat(response.getBody().getErrors().get(0).getError().equals("Validation Error"));

        logger.info("Estado : {}", response.getStatusCode());
        logger.info("Error  : {}", response.getBody().getErrors().get(0).getError());
        logger.info("Descripcion   : {}", response.getBody().getErrors().get(0).getDescription());

    }

}