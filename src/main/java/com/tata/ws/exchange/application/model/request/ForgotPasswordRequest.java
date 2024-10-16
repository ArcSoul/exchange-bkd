package com.tata.ws.exchange.application.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Schema(description = "Solicitud para recuperar la contraseña")
public class ForgotPasswordRequest {
    @Email(message = "El email debe tener el formato correcto")
    @JsonProperty("correo")
    @Schema(description = "Correo electrónico del usuario", example = "usuario@correo.com")
    private String email;
}
