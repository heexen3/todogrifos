package com.todogrifos.ventasms.exception;

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
     * Captura cuando el folio de la boleta ya existe en el sistema.
     * Retorna un código HTTP 409 Conflict.
     */
    @ExceptionHandler(FolioDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> handleFolioDuplicadoException(FolioDuplicadoException ex) {
        log.warn("Excepción de validación comercial controlada: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("status", HttpStatus.CONFLICT.value());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Captura violaciones lógicas de negocio (Cliente inválido, stock insuficiente, caída de microservicios).
     * Retorna un código HTTP 400 Bad Request debido a petición inviable.
     */
    @ExceptionHandler(VentaInvalidaException.class)
    public ResponseEntity<Map<String, Object>> handleVentaInvalidaException(VentaInvalidaException ex) {
        log.warn("Rechazo de procesamiento transaccional: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Captura los fallos estructurales de Bean Validation (@Valid en el Controller).
     * Extrae las restricciones rotas del DTO y arma un mapa detallado de campos.
     * Retorna un código HTTP 400 Bad Request.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String campo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            errores.put(campo, mensaje);
            log.warn("Contrato DTO roto - Campo: '{}', Mensaje: '{}'", campo, mensaje);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    /**
     * Captura cualquier otra excepción no controlada en el hilo de ejecución para proteger el sistema.
     * Retorna un código HTTP 500 Internal Server Error escribiendo el stacktrace en el servidor.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("ERROR CRÍTICO NO CONTROLADO EN VENTAS-MS: ", ex);

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Error interno e inesperado en el módulo de facturación y ventas. Contacte soporte.");
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}