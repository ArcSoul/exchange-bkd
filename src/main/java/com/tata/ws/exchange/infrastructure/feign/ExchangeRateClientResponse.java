package com.tata.ws.exchange.infrastructure.feign;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
@RequiredArgsConstructor
public class ExchangeRateClientResponse {
    @JsonProperty("base_code")
    private String baseCode;
    @JsonProperty("rates")
    private Map<String, Double> exchangeRates;

    // Metodo para obtener el tipo de cambio por código de moneda
    public Double getExchangeRateByCurrency(String currencyCode) {
        return exchangeRates.get(currencyCode);
    }
}
