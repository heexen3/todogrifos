package com.todogrifos.despachosms.exception;

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
     * Captura cuando el código de seguimiento o la orden logística de la venta ya existen.
     * Retorna un código HTTP 409 Conflict.
     */
    @ExceptionHandler(DespachoDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> handleDespachoDuplicadoException(DespachoDuplicadoException ex) {
        log.warn("Excepción de validación de negocio: Despacho Duplicado -> {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("status", HttpStatus.CONFLICT.value());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Captura cuando no se encuentra la orden logística solicitada.
     * Retorna un código HTTP 404 Not Found.
     */
    @ExceptionHandler(DespachoNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleDespachoNotFoundException(DespachoNotFoundException ex) {
        log.warn("Búsqueda fallida en BD: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("status", HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Captura los fallos estructurales de Bean Validation (@Valid en el Controller).
     * Retorna un código HTTP 400 Bad Request mapeando campos.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String campo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            errores.put(campo, mensaje);
            log.warn("Contrato DTO roto en logística - Campo: '{}', Mensaje: '{}'", campo, mensaje);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    /**
     * Captura cualquier falla imprevista en el hilo de ejecución para proteger el sistema.
     * Retorna un código HTTP 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("ERROR CRÍTICO NO CONTROLADO EN DESPACHOS-MS: ", ex);

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Error interno en el módulo de distribución logística. Contacte soporte.");
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}