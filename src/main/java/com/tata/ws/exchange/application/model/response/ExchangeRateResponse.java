package com.tata.ws.exchange.application.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Respuesta con el resultado de la conversión de tipo de cambio")
public class ExchangeRateResponse {
    @JsonProperty("monto")
    @Schema(description = "Monto original ingresado", example = "100.0")
    private double amount;

    @JsonProperty("monto_con_tipo_de_cambio")
    @Schema(description = "Monto resultante después de aplicar el tipo de cambio", example = "85.0")
    private double amountWithExchangeRate;

    @JsonProperty("moneda_origen")
    @Schema(description = "Moneda de origen (ISO 4217)", example = "USD")
    private String originCurrency;

    @JsonProperty("moneda_destino")
    @Schema(description = "Moneda de destino (ISO 4217)", example = "EUR")
    private String destinationCurrency;

    @JsonProperty("tipo_de_cambio")
    @Schema(description = "Tipo de cambio aplicado", example = "0.85")
    private double exchangeRate;
}
