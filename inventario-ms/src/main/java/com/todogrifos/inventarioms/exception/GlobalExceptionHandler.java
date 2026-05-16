package com.todogrifos.inventarioms.exception;

import feign.FeignException;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ERROR 404 - Recurso No Encontrado
    @ExceptionHandler(InventarioNotFoundException.class)
    public ResponseEntity<String> manejarNoEncontrado(InventarioNotFoundException ex) {
        log.warn("Inventario no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // ERROR 409 - Conflicto (SKU ya existe en inventario)
    @ExceptionHandler(SkuDuplicadoException.class)
    public ResponseEntity<String> manejarSkuDuplicado(SkuDuplicadoException ex) {
        log.error("Conflicto de integridad: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    // ERROR 400 - Bad Request (Stock insuficiente para una venta)
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<String> manejarStockInsuficiente(InsufficientStockException ex) {
        log.warn("Petición rechazada: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // ERROR 400 - Validaciones de campos (@Valid en los DTOs)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidaciones(MethodArgumentNotValidException ex) {
        int erroresTotales = ex.getBindingResult().getErrorCount();
        log.warn("Errores de validación detectados ({}): ", erroresTotales);

        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            log.debug("Campo '{}': {}", error.getField(), error.getDefaultMessage());
            errores.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    // ERROR 500 - Error Interno del Servidor (Cualquier otra excepción no controlada)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> manejarErrorGeneral(Exception ex) {
        log.error("ERROR NO CONTROLADO EN INVENTARIO-MS: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ocurrió un error inesperado en el sistema de inventario. Contacte al administrador.");
    }

    @ExceptionHandler(InvalidStockQuantityException.class)
    public ResponseEntity<String> manejarCantidadInvalida(InvalidStockQuantityException ex) {
        log.warn("Cantidad de stock incongruente: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<Map<String, Object>> handleFeignNotFoundException(FeignException.NotFound ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Producto no encontrado en el catálogo maestro remoto.");
        response.put("status", HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(RetryableException.class)
    public ResponseEntity<Map<String, Object>> handleFeignRetryableException(RetryableException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "El servicio de catálogo de productos no se encuentra disponible temporalmente. Intente más tarde.");
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE) // Código HTTP 503
                .body(response);
    }

    @ExceptionHandler(FeignException.ServiceUnavailable.class)
    public ResponseEntity<Map<String, Object>> handleFeignServiceUnavailable(FeignException.ServiceUnavailable ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "El servicio de catálogo de productos no está disponible en este momento. Intente más tarde.");
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE) // Código HTTP 503
                .body(response);
    }
}