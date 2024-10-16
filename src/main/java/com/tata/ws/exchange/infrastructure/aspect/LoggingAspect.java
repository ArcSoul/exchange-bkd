package com.tata.ws.exchange.infrastructure.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    // Nombres de parámetros sensibles que deben ser enmascarados
    private static final Set<String> SENSITIVE_KEYS = new HashSet<>(Arrays.asList("token", "authorization", "password"));


    @Around("execution(public * com.tata.ws.exchange.application.service..*.*(..))")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
        // Obtenemos el nombre de la clase
        String className = joinPoint.getTarget().getClass().getSimpleName();

        // Obtenemos el nombre del metodo
        String methodName = joinPoint.getSignature().getName();

        // Obtenemos los parámetros del metodo
        String methodArgs = sanitizeArgs(joinPoint.getArgs());

        log.info("[{}][{}][TATA] request: {}", className, methodName, methodArgs);

        Object result;
        long startTime = System.currentTimeMillis();

        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            log.error("[{}][{}][TATA_FINEX] Exception: {}", className, methodName, e.getMessage(), e);
            throw e;
        }

        long timeTaken = System.currentTimeMillis() - startTime;

        // Sanitizar la respuesta antes de registrarla
        String sanitizedResult = sanitizeObject(result);

        log.info("[{}][{}][TATA_FINOK] timeTaken: [{} ms], response: {}", className, methodName, timeTaken, sanitizedResult);

        return result;
    }

    // Metodo para sanitizar parámetros
    private String sanitizeArgs(Object[] args) {
        if (args == null) {
            return "[]";
        }
        return Arrays.stream(args)
                .map(this::sanitizeObject)
                .collect(Collectors.joining(", ", "[", "]"));
    }

    private String sanitizeObject(Object arg) {
        if (arg == null) {
            return "null";
        }

        if (arg instanceof String) {
            // Sanitiza si el argumento contiene una clave sensible
            return sanitizeString((String) arg);
        }

        // Verificamos si es una colección (List, Set, etc.)
        if (arg instanceof Iterable<?>) {
            return sanitizeIterable((Iterable<?>) arg);
        }

        // Verificamos si es un Map (por si necesitamos manejar mapas)
        if (arg instanceof Map<?, ?>) {
            return sanitizeMap((Map<?, ?>) arg);
        }

        // Si es una instancia de una clase personalizada, intenta sanitizar por campo
        return sanitizeCustomObject(arg);
    }

    // Sanitizacion de listas y sets
    private String sanitizeIterable(Iterable<?> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .map(this::sanitizeObject)  // Sanitizar cada elemento de la lista
                .collect(Collectors.joining(", ", "[", "]"));
    }

    // Sanitizacion de mapas
    private String sanitizeMap(Map<?, ?> map) {
        return map.entrySet().stream()
                .map(entry -> sanitizeObject(entry.getKey()) + "=" + sanitizeObject(entry.getValue()))
                .collect(Collectors.joining(", ", "{", "}"));
    }

    // Sanitizacion para cadenas
    private String sanitizeString(String argStr) {
        for (String sensitiveKey : SENSITIVE_KEYS) {
            if (argStr.toLowerCase().contains(sensitiveKey)) {
                return sensitiveKey + "=*******";
            }
        }
        return argStr;  // Si no es sensible, devuelve tal cual
    }

    // Sanitiza cada objeto
    private String sanitizeCustomObject(Object arg) {
        try {
            Field[] fields = arg.getClass().getDeclaredFields();
            StringBuilder sanitizedObject = new StringBuilder(arg.getClass().getSimpleName() + "{");

            for (Field field : fields) {
                field.setAccessible(true);  // Permitir acceso a campos privados
                Object value = field.get(arg);
                String fieldName = field.getName();

                if (SENSITIVE_KEYS.contains(fieldName.toLowerCase())) {
                    sanitizedObject.append(fieldName).append("=*******");
                } else {
                    sanitizedObject.append(fieldName).append("=").append(value);
                }
                sanitizedObject.append(", ");
            }
            // Elimina la coma y espacio al final
            if (sanitizedObject.length() > 2) {
                sanitizedObject.setLength(sanitizedObject.length() - 2);
            }
            sanitizedObject.append("}");
            return sanitizedObject.toString();
        } catch (IllegalAccessException e) {
            log.error("Error filtrando el objeto: {}", e.getMessage(), e);
            return arg.toString();  // En caso de error, devuelve la representación por defecto
        }
    }

}
