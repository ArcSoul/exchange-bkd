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
@Schema(description = "Respuesta para inicio de sesión")
public class UserSignInResponse {
    @JsonProperty("correo")
    @Schema(description = "Correo electrónico del usuario", example = "usuario@correo.com")
    private String email;

    @JsonProperty("token")
    @Schema(description = "Token JWT para autenticación", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
}
