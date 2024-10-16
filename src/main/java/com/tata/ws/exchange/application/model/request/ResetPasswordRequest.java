package com.tata.ws.exchange.application.model.request;

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
@Schema(description = "Solicitud para restablecer la contraseña")
public class ResetPasswordRequest {
    @JsonProperty("nueva_clave")
    @Schema(description = "Nueva contraseña", example = "nuevaClave123")
    private String password;

    @JsonProperty("token")
    @Schema(description = "Token de restablecimiento", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
}
