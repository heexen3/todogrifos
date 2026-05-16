package com.todogrifos.ventasms.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "inventario-ms", path = "/api/inventario")
public interface InventarioClient {

    // Reutiliza la lógica de retiro de stock de inventario-ms pasándole el SKU en la URL y la cantidad en el DTO
    @PutMapping("/{sku}/retirar")
    ResponseEntity<Map<String, Object>> retirarStock(
            @PathVariable String sku,
            @RequestBody Map<String, Object> stockUpdateDto
    );
}