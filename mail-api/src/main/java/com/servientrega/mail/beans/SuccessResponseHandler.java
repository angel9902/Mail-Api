/**
 * 
 */
package com.servientrega.mail.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.ExchangeProperty;
import org.apache.camel.Handler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.servientrega.mail.dto.MailResponse;

import io.swagger.annotations.ApiModel;

/**
 * @author Juan Carlos
 *
 */
@XmlRootElement
@JsonAutoDetect
@JsonSerialize
@ApiModel(description = "Response DTO Object OK")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Component
public class SuccessResponseHandler implements Serializable {

    private static final long serialVersionUID = 1L;

    @Handler
    public MailResponse createResponse(@ExchangeProperty("SEND_MAIL_STATUS") List<Map<String, String>> list) {
        MailResponse mailResponse = new MailResponse();
        mailResponse.setData(list);
        mailResponse.setSuccess(Boolean.TRUE);
        return mailResponse;
    }

}
