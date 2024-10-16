package com.tata.ws.exchange.infrastructure.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Schema(description = "Respuesta estándar de error")
public class ErrorResponse {
    @JsonProperty("tiempo")
    @Schema(description = "Fecha y hora del error", example = "2024-10-15T23:59:00")
    private LocalDateTime timestamp;

    @JsonProperty("estado")
    @Schema(description = "Código de estado HTTP", example = "400")
    private int status;

    @JsonProperty("error")
    @Schema(description = "Descripción del error", example = "Bad Request")
    private String error;

    @JsonProperty("mensaje")
    @Schema(description = "Mensaje de error", example = "Solicitud inválida")
    private String message;

    @JsonProperty("ruta")
    @Schema(description = "Ruta donde ocurrió el error", example = "/api/exchange-rate/apply")
    private String path;
}
