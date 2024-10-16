package com.tata.ws.exchange.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "exchangeRateClient", url = "${actuator.health.api.external.test}")
public interface ExchangeRateClient {

    @GetMapping("/codes")
    SupportedCodesResponse getExchangeRate();

}
