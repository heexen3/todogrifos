package com.todogrifos.productosms.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // ERROR 404 - NOT FOUND
    @ExceptionHandler({
            ProductoNotFoundException.class,
            MarcaNotFoundException.class,
            CategoriaNotFoundException.class
    })
    public ResponseEntity<String> manejarRecursoNoEncontrado(RuntimeException ex) {
        // usamos log.warn porque es un error esperado del cliente
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // ERROR 409 CONFLICT
    @ExceptionHandler(SkuDuplicadoException.class)
    public ResponseEntity<String> manejarSkuDuplicado(SkuDuplicadoException ex) {
        // usamos log.error porque indica un intento de violar la integridad de datos
        log.error("Conflicto de integridad de datos: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    // ERROR 400 validaciones de Bean Validation (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> argumentoNoValido(MethodArgumentNotValidException ex) {
        int totalErrores = ex.getBindingResult().getErrorCount();
        log.warn("Petición rechazada: Se encontraron {} errores de validación", totalErrores);

        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            // Log de nivel debug para no saturar la consola pero permitir rastreo técnico
            log.debug("Campo '{}': {}", error.getField(), error.getDefaultMessage());
            errores.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    // ERROR 500 -
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> manejarErrorGeneral(Exception ex) {
        // esto registra errores inesperados (como caída de BD o NullPointer)
        log.error("ERROR INTERNO DEL SISTEMA: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ocurrió un error inesperado. Por favor, contacte al administrador.");
    }
}