package com.servientrega.mail.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangeProperty;
import org.apache.camel.Handler;
import org.springframework.stereotype.Component;

import com.servientrega.mail.dto.Recipient;

/**
 * 
 */
@Component
public class ProcessMailResponseHandler {

    @Handler
    public void process(Exchange exchange, @ExchangeProperty("RECIPIENT") Recipient recipient,
            @ExchangeProperty("SEND_MAIL_STATUS") List<Map<String, String>> list) throws Exception {
        Map<String, String> map = new HashMap<>();
        if (list == null) {
            list = new ArrayList<>();
        }

        if (exchange.getProperty(Exchange.EXCEPTION_CAUGHT) != null) {
            Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
            map.put("email", recipient.getEmail());
            map.put("status", exception.getMessage());
        } else {
            map.put("email", recipient.getEmail());
            map.put("status", "Sent successfully");
        }
        list.add(map);
        exchange.setProperty("SEND_MAIL_STATUS", list);
    }

}
