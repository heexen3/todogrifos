package com.todogrifos.devolucionesms.exception;

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
     * Captura cuando el folio de la Nota de Crédito ya fue emitido con anterioridad.
     * Retorna un código HTTP 409 Conflict.
     */
    @ExceptionHandler(FolioDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> handleFolioDuplicadoException(FolioDuplicadoException ex) {
        log.warn("Validación de post-venta controlada - Folio Duplicado: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("status", HttpStatus.CONFLICT.value());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Captura fallos de reglas de negocio, como inconsistencias con ventas u hilos remotos de OpenFeign.
     * Retorna un código HTTP 422 Unprocessable Entity (Entidad No Procesable).
     */
    @ExceptionHandler(DevolucionInvalidaException.class)
    public ResponseEntity<Map<String, Object>> handleDevolucionInvalidaException(DevolucionInvalidaException ex) {
        log.warn("Operación logístico-comercial rechazada: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    /**
     * Intercepta automáticamente las violaciones de las restricciones de Bean Validation en los DTOs.
     * Retorna un código HTTP 400 Bad Request con el mapa detallado de campos inválidos.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String campo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            errores.put(campo, mensaje);
            log.warn("Contrato DTO de Devolución roto - Campo: '{}', Mensaje: '{}'", campo, mensaje);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    /**
     * Intercepta fallos generales de infraestructura o caídas críticas de servicios.
     * Retorna un código HTTP 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("ERROR CRÍTICO NO CONTROLADO EN DEVOLUCIONES-MS: ", ex);

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Error interno inesperado en el módulo de post-venta. Contacte al administrador.");
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}