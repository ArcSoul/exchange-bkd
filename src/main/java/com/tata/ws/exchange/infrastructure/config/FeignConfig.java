package com.tata.ws.exchange.infrastructure.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.tata.ws.exchange.infrastructure.feign")
public class FeignConfig {
}
