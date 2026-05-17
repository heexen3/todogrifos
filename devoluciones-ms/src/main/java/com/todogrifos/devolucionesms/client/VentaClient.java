package com.todogrifos.devolucionesms.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "ventas-ms")
public interface VentaClient {

    /**
     * Valida remotamente la existencia de la boleta o factura original.
     */
    @GetMapping("/api/ventas/{id}")
    ResponseEntity<Map<String, Object>> verificarVentaPorId(@PathVariable("id") Long id);
}