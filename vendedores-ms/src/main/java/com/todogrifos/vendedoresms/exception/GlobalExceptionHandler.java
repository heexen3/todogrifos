package com.todogrifos.vendedoresms.exception;

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
     * Captura cuando se intenta registrar un RUT o código de empleado existente.
     * Retorna un código HTTP 409 Conflict.
     */
    @ExceptionHandler(VendedorDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> handleVendedorDuplicadoException(VendedorDuplicadoException ex) {
        log.warn("Validación comercial controlada - Personal Duplicado: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("status", HttpStatus.CONFLICT.value());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Captura cuando no se encuentra el vendedor solicitado por ID o Código.
     * Retorna un código HTTP 404 Not Found.
     */
    @ExceptionHandler(VendedorNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleVendedorNotFoundException(VendedorNotFoundException ex) {
        log.warn("Búsqueda de personal fallida: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("status", HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Intercepta automáticamente las violaciones de Bean Validation en los DTOs.
     * Retorna un código HTTP 400 Bad Request con el mapa detallado de campos.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String campo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            errores.put(campo, mensaje);
            log.warn("Contrato DTO de Personal roto - Campo: '{}', Mensaje: '{}'", campo, mensaje);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    /**
     * Intercepta fallos generales imprevistos de infraestructura.
     * Retorna un código HTTP 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("ERROR CRÍTICO NO CONTROLADO EN VENDEDORES-MS: ", ex);

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Error interno e inesperado en el módulo de gestión de personal. Contacte soporte.");
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}