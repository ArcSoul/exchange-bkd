package com.tata.ws.exchange.application.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Schema(description = "Respuesta para la solicitud de recuperación de contraseña")
public class ForgotPasswordResponse {
    @JsonProperty("resultado")
    @Schema(description = "Resultado de la operación", example = "true")
    private boolean result;
}
