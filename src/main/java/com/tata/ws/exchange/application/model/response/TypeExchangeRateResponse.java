package com.tata.ws.exchange.application.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Tipo de cambio disponible")
public class TypeExchangeRateResponse {
    @JsonProperty("codigo")
    @Schema(description = "Código de la moneda (ISO 4217)", example = "USD")
    private String code;

    @JsonProperty("nombre")
    @Schema(description = "Nombre de la moneda", example = "Dólar estadounidense")
    private String name;
}
