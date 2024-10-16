package com.tata.ws.exchange.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "exchangeRateFreeClient", url = "${actuator.health.api.external.free}")
public interface ExchangeRateFreeClient {

    @GetMapping("/latest/{code}")
    ExchangeRateClientResponse getLatestExchangeRate(@PathVariable("code") String code);

}
