package com.todogrifos.comprasms.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura cuando el número de factura del proveedor ya fue registrado previamente.
     * Retorna un código HTTP 409 Conflict.
     */
    @ExceptionHandler(FacturaDuplicadaException.class)
    public ResponseEntity<Map<String, Object>> handleFacturaDuplicadaException(FacturaDuplicadaException ex) {
        log.warn("Validación comercial: Intento de ingresar un documento duplicado. Mensaje: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("status", HttpStatus.CONFLICT.value());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Captura inconsistencias lógicas de negocio o fallos en las dependencias distribuidas
     * (caídas de red de inventario-ms, SKUs inexistentes, etc.).
     * Retorna un código HTTP 400 Bad Request.
     */
    @ExceptionHandler(CompraInvalidaException.class)
    public ResponseEntity<Map<String, Object>> handleCompraInvalidaException(CompraInvalidaException ex) {
        log.warn("Procesamiento transaccional rechazado: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Intercepta automáticamente los fallos de Bean Validation en los DTOs de entrada.
     * Retorna un código HTTP 400 Bad Request detallando los campos inválidos.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String campo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            errores.put(campo, mensaje);
            log.warn("Fallo de validación estructural JSR 380 - Campo: '{}', Mensaje: '{}'", campo, mensaje);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    /**
     * Captura de fallos imprevistos de infraestructura para blindar la API.
     * Retorna un código HTTP 500 Internal Server Error guardando el stacktrace completo en el servidor.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("ERROR CRÍTICO NO CONTROLADO EN COMPRAS-MS: ", ex);

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Error interno en el módulo de compras y abastecimiento. Contacte soporte.");
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}