package com.todogrifos.inventarioms.client;

import com.todogrifos.inventarioms.dto.ProductoDTO; // Deberás crear este DTO en inventario
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Se conecta al nombre registrado en Eureka
@FeignClient(name = "productos-ms")
public interface ProductoClient {

    @GetMapping("/api/productos/sku/{sku}")
    ProductoDTO getProductoBySku(@PathVariable("sku") String sku);
}