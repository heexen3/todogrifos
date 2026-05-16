package com.todogrifos.ventasms.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "clientes-ms", path = "/api/clientes")
public interface ClienteClient {

    // Consume el endpoint de clientes para verificar si existe
    @GetMapping("/{id}")
    ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable("id") Long id);
}