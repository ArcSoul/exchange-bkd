package com.tata.ws.exchange.application.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Schema(description = "Solicitud para obtener los registros de conversiones")
public class RecordExchangeRateRequest {
    @JsonProperty("correo")
    @Schema(description = "Correo electrónico del usuario", example = "usuario@correo.com")
    private String username;
}
