package com.todogrifos.comprasms.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "inventario-ms", path = "/api/inventario")
public interface InventarioClient {

    /**
     * Consume el endpoint de inventario-ms para incrementar las existencias de un artículo.
     * En el RequestBody le pasamos la cantidad comprada que se va a sumar al stock actual.
     */
    @PutMapping("/{sku}/ingresar")
    ResponseEntity<Map<String, Object>> ingresarStock(
            @PathVariable("sku") String sku,
            @RequestBody Map<String, Object> stockUpdateDto
    );
}