package com.tata.ws.exchange.interfaces.rest;

import com.tata.ws.exchange.application.model.request.ExchangeRateRequest;
import com.tata.ws.exchange.application.model.request.RecordExchangeRateRequest;
import com.tata.ws.exchange.application.model.response.ExchangeRateResponse;
import com.tata.ws.exchange.application.model.response.RecordExchangeRateResponse;
import com.tata.ws.exchange.application.model.response.TypeExchangeRateResponse;
import com.tata.ws.exchange.application.service.ExchangeService;
import com.tata.ws.exchange.infrastructure.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exchange-rate")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Exchange Rate", description = "API para gestionar tipos de cambio")
public class ExchangeRateController {
    private final ExchangeService exchangeService;

    @PostMapping("/apply")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Aplica el tipo de cambio a un monto",
            description = "Realiza la conversión de una moneda a otra aplicando el tipo de cambio.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conversión realizada exitosamente", content = @Content(schema = @Schema(implementation = ExchangeRateResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ExchangeRateResponse applyExchangeRate(@Valid @RequestBody ExchangeRateRequest request) {
        return exchangeService.applyExchangeRate(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Obtiene los tipos de cambio disponibles",
            description = "Devuelve una lista de los tipos de cambio actuales.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de tipos de cambio", content = @Content(schema = @Schema(implementation = TypeExchangeRateResponse.class))),
                    @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public List<TypeExchangeRateResponse> getExchangeRate() {
        return exchangeService.getExchangeRate();
    }

    @PostMapping("/records")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Consulta registros de conversiones",
            description = "Devuelve los registros históricos de conversiones de tipo de cambio realizadas por el usuario.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de registros de conversión", content = @Content(schema = @Schema(implementation = RecordExchangeRateResponse.class))),
                    @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public List<RecordExchangeRateResponse> recordExchangeRate(@Valid @RequestBody RecordExchangeRateRequest request) {
        return exchangeService.recordExchangeRate(request);
    }

}
