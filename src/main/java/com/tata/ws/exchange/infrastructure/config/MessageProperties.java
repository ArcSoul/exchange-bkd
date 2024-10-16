package com.tata.ws.exchange.infrastructure.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Setter
@Getter
@Configuration
@PropertySource("classpath:messages.properties")
@ConfigurationProperties(prefix = "exchange")
public class MessageProperties {

    private final Auth auth = new Auth();

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class Auth {
        private String userNotFound;
    }
}
