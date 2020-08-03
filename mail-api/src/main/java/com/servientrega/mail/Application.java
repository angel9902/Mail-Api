package com.servientrega.mail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

/**
 *
 * @author Asssert Solutions S.A.S
 */
@ComponentScan
@SpringBootApplication
@EnableAutoConfiguration
@EnableConfigurationProperties
@ComponentScans(value = { @ComponentScan(basePackages = "com.servientrega.audit"),
        @ComponentScan(basePackages = "com.servientrega.commons") })
public class Application {

    /**
     * A main method to start this application.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
