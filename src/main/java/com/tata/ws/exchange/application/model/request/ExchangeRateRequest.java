package com.tata.ws.exchange.application.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Solicitud para aplicar el tipo de cambio")
public class ExchangeRateRequest {
    @JsonProperty("monto")
    @Schema(description = "Monto original a convertir", example = "100.0")
    private double amount;
    @JsonProperty("moneda_origen")
    @Schema(description = "Moneda de origen (ISO 4217)", example = "USD")
    private String originCurrency;
    @JsonProperty("moneda_destino")
    @Schema(description = "Moneda de destino (ISO 4217)", example = "EUR")
    private String destinationCurrency;
}