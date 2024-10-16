package com.tata.ws.exchange.infrastructure.metric;

import com.tata.ws.exchange.infrastructure.feign.ExchangeRateFreeClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExternalApiHealthIndicator implements HealthIndicator {

    private final ExchangeRateFreeClient exchangeRateClient;

    @Override
    public Health health() {
        try {
            exchangeRateClient.getLatestExchangeRate("USD");
            return Health.up().withDetail("External API", "Disponible").build();
        } catch (Exception e) {
            return Health.down().withDetail("External API", "No Disponible").build();
        }
    }
}
