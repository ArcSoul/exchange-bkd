package com.tata.ws.exchange.interfaces.rest;

import com.tata.ws.exchange.application.model.request.ForgotPasswordRequest;
import com.tata.ws.exchange.application.model.request.ResetPasswordRequest;
import com.tata.ws.exchange.application.model.request.UserSignInRequest;
import com.tata.ws.exchange.application.model.request.UserSignUpRequest;
import com.tata.ws.exchange.application.model.response.ForgotPasswordResponse;
import com.tata.ws.exchange.application.model.response.ResetPasswordResponse;
import com.tata.ws.exchange.application.model.response.UserSignInResponse;
import com.tata.ws.exchange.application.model.response.UserSignUpResponse;
import com.tata.ws.exchange.application.service.AuthenticationService;
import com.tata.ws.exchange.infrastructure.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API para gestionar autenticación de usuarios")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Registro de usuario",
            description = "Permite registrar un nuevo usuario en la aplicación.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente", content = @Content(schema = @Schema(implementation = UserSignUpResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public UserSignUpResponse signUp(@Valid @RequestBody UserSignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Inicio de sesión",
            description = "Autentica a un usuario y devuelve un token JWT para futuras solicitudes.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso", content = @Content(schema = @Schema(implementation = UserSignInResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public UserSignInResponse signIn(@Valid @RequestBody UserSignInRequest request) {
        return authenticationService.signIn(request);
    }

    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Solicitud de recuperación de contraseña",
            description = "Envía un enlace de recuperación de contraseña al correo del usuario.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Solicitud de recuperación de contraseña enviada", content = @Content(schema = @Schema(implementation = ForgotPasswordResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ForgotPasswordResponse forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return authenticationService.forgotPassword(request);
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Restablecimiento de contraseña",
            description = "Permite al usuario restablecer su contraseña mediante un token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contraseña restablecida exitosamente", content = @Content(schema = @Schema(implementation = ResetPasswordResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResetPasswordResponse resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return authenticationService.resetPassword(request);
    }

}
