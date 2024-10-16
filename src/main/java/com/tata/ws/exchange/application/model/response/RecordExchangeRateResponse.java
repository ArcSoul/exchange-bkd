package com.tata.ws.exchange.application.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta con los registros de conversiones de tipo de cambio")
public class RecordExchangeRateResponse {
    @JsonProperty("monto")
    @Schema(description = "Monto original", example = "100.0")
    private double amount;

    @JsonProperty("monto_con_tipo_de_cambio")
    @Schema(description = "Monto resultante con el tipo de cambio", example = "85.0")
    private double amountWithExchangeRate;

    @JsonProperty("moneda_origen")
    @Schema(description = "Moneda de origen", example = "USD")
    private String originCurrency;

    @JsonProperty("moneda_destino")
    @Schema(description = "Moneda de destino", example = "EUR")
    private String destinationCurrency;

    @JsonProperty("tipo_de_cambio")
    @Schema(description = "Tipo de cambio aplicado", example = "0.85")
    private double exchangeRate;

    @JsonProperty("fecha")
    @Schema(description = "Fecha de la conversión", example = "2024-10-15T23:23:32")
    private LocalDateTime date;
}
