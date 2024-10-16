package com.tata.ws.exchange.infrastructure.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception, HttpServletRequest request) {
        ErrorResponse response = null;

        if (exception instanceof BadCredentialsException) {
            response = new ErrorResponse();
            response.setTimestamp(LocalDateTime.now());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setError("El correo o la clave es incorrecta");
            response.setMessage(exception.getMessage());
            response.setPath(request.getRequestURI());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        if (exception instanceof MethodArgumentNotValidException) {
            List<String> errors = ((MethodArgumentNotValidException) exception).getBindingResult().getFieldErrors()
                    .stream().map(FieldError::getDefaultMessage).toList();

            response = new ErrorResponse();
            response.setTimestamp(LocalDateTime.now());
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setError(errors.stream().reduce("", (errorParcial, error) -> errorParcial.concat("; ").concat(error)));
            response.setMessage(exception.getMessage());
            response.setPath(request.getRequestURI());
        }

        if (exception instanceof AccountStatusException) {
            response = new ErrorResponse();
            response.setTimestamp(LocalDateTime.now());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setError("La cuenta esta bloqueada");
            response.setMessage(exception.getMessage());
            response.setPath(request.getRequestURI());
        }

        if (exception instanceof AccessDeniedException) {
            response = new ErrorResponse();
            response.setTimestamp(LocalDateTime.now());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setError("No estas autorizado para acceder a este recurso");
            response.setMessage(exception.getMessage());
            response.setPath(request.getRequestURI());
        }

        if (exception instanceof SignatureException) {
            response = new ErrorResponse();
            response.setTimestamp(LocalDateTime.now());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setError("La firma del JWT es invalida");
            response.setMessage(exception.getMessage());
            response.setPath(request.getRequestURI());
        }

        if (exception instanceof MalformedJwtException) {
            response = new ErrorResponse();
            response.setTimestamp(LocalDateTime.now());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setError("El token JWT no tiene el formato correcto");
            response.setMessage(exception.getMessage());
            response.setPath(request.getRequestURI());
        }

        if (exception instanceof ExpiredJwtException) {
            response = new ErrorResponse();
            response.setTimestamp(LocalDateTime.now());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setError("La firma del JWT a expirado");
            response.setMessage(exception.getMessage());
            response.setPath(request.getRequestURI());
        }

        if (response == null) {
            response = new ErrorResponse();
            response.setTimestamp(LocalDateTime.now());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setError("Ha ocurrido un error desconocido en el servidor.");
            response.setMessage(exception.getMessage());
            response.setPath(request.getRequestURI());
        }

        return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
    }
}
