package com.todogrifos.devolucionesms.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "inventario-ms")
public interface InventarioClient {
    /**
     * Incrementa de forma remota el stock en bodega cuando una devolución vuelve a estar apta para la venta.
     */
    @PutMapping("/api/inventario/sku/{sku}/adicionar")
    ResponseEntity<Map<String, Object>> adicionarStock(
            @PathVariable("sku") String sku,
            @RequestBody Map<String, Object> requestBody);
}