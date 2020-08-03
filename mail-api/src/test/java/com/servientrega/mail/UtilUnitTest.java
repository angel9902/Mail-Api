package com.servientrega.mail;

import com.servientrega.mail.dto.Attachment;
import com.servientrega.mail.dto.Parameters;
import com.servientrega.mail.util.AttachmentUtil;
import com.servientrega.mail.util.Util;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UnitTestConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {
        "server.port=8082"})
public class UtilUnitTest {

    @Autowired
    private CamelContext camelContext;
    @Autowired
    private Util util;
    @Autowired
    private AttachmentUtil attachmentUtil;

    @Test
    public void replaceBody() {
        Exchange exchange = new DefaultExchange(camelContext);
        List<Parameters> parameters = new ArrayList<>();
        parameters.add(new Parameters("parameter", "Value"));
        util.replaceBody("Text replace: ${parameter}", parameters, exchange);

        assertNotNull(exchange.getProperty("MAIL_MESSAGE"));
    }

    @Test
    public void validateAttachment() {
        Exchange exchange = new DefaultExchange(camelContext);
        List<Attachment> attachments = new ArrayList<>();
        attachments.add(new Attachment("file.pdf", "fdsfsdfdsfds"));
        try {
            attachmentUtil.attachmentValidate(attachments, exchange);
            assertNotNull(exchange.getProperty("MAIL_ATTACHMENTS"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
