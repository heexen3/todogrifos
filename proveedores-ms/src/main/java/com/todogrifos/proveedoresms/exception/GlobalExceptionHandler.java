package com.todogrifos.proveedoresms.exception;

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
     * Captura cuando el RUT o Email corporativo del proveedor ya existen.
     * Retorna un código HTTP 409 Conflict.
     */
    @ExceptionHandler(ProveedorDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> handleProveedorDuplicadoException(ProveedorDuplicadoException ex) {
        log.warn("Validación de negocio controlada - Proveedor Duplicado: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("status", HttpStatus.CONFLICT.value());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Captura cuando no se encuentra el proveedor solicitado por ID o RUT.
     * Retorna un código HTTP 404 Not Found.
     */
    @ExceptionHandler(ProveedorNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProveedorNotFoundException(ProveedorNotFoundException ex) {
        log.warn("Búsqueda fallida controlada: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("status", HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Intercepta de forma automática las violaciones de las anotaciones JSR 380 en los DTOs.
     * Retorna un código HTTP 400 Bad Request con el mapa detallado de campos inválidos.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String campo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            errores.put(campo, mensaje);
            log.warn("Contrato DTO de Proveedor roto - Campo: '{}', Mensaje: '{}'", campo, mensaje);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    /**
     * Intercepta fallos generales de infraestructura no controlados.
     * Retorna un código HTTP 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("ERROR CRÍTICO NO CONTROLADO EN PROVEEDORES-MS: ", ex);

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Error interno e inesperado en el módulo de proveedores. Contacte soporte técnico.");
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}