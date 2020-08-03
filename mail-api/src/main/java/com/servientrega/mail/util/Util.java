/**
 * 
 */
package com.servientrega.mail.util;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangeProperty;
import org.springframework.stereotype.Component;

import com.servientrega.mail.dto.Parameters;

/**
 * 
 * @author Assert Solutions
 *
 */
@Component
public class Util {

    public void replaceBody(@ExchangeProperty("MAIL_MESSAGE") String message,
            @ExchangeProperty("MAIL_PARAMETERS") List<Parameters> parameters, Exchange exchange) {
        Optional.ofNullable(parameters).orElseGet(Collections::emptyList).stream().forEach(p -> {
            String msg = message.replaceAll("\\$\\{" + p.getKey() + "}", String.valueOf(p.getValue()));
            exchange.setProperty("MAIL_MESSAGE", msg);
        });
    }
}
