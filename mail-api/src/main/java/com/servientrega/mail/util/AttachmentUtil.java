/**
 * 
 */
package com.servientrega.mail.util;

import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Base64;
import java.util.List;

import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangeProperty;
import org.apache.camel.Handler;
import org.apache.camel.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.servientrega.mail.dto.Attachment;

/**
 * @author Juan Carlos
 *
 */
@Component
public class AttachmentUtil {

    private static final String MAIL_ATTACHMENT_PROPERTY = "MAIL_ATTACHMENTS";

    @Handler
    public void attachmentValidate(@ExchangeProperty(MAIL_ATTACHMENT_PROPERTY) List<Attachment> attachments,
            Exchange exchange) throws Exception {
        Message in = exchange.getIn();
        if (attachments != null && !attachments.isEmpty()) {
            for (Attachment attachment : attachments) {
                FileNameMap fileNameMap = URLConnection.getFileNameMap();
                String mimeType = fileNameMap.getContentTypeFor(attachment.getName()
                        .substring(attachment.getName().indexOf('.'), attachment.getName().length()));
                if (StringUtils.isEmpty(mimeType)) {
                    mimeType = "application/octet-stream";
                }
                byte[] decoded = Base64.getDecoder().decode(attachment.getValue());
                in.addAttachment(attachment.getName(), new DataHandler(new ByteArrayDataSource(decoded, mimeType)));
            }
        }
        exchange.setProperty(MAIL_ATTACHMENT_PROPERTY, attachments);
    }
}
