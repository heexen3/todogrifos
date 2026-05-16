package com.todogrifos.clientesms.controller;

import com.todogrifos.clientesms.dto.ClienteCreateDTO;
import com.todogrifos.clientesms.dto.ClienteResponseDTO;
import com.todogrifos.clientesms.service.ClienteService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> registrarCliente(@Valid @RequestBody ClienteCreateDTO dto) {
        log.info("Petición HTTP recibida: POST /api/clientes para registrar el RUT: {}", dto.getRut());

        ClienteResponseDTO nuevoCliente = clienteService.registrarCliente(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<ClienteResponseDTO> obtenerPorRut(@PathVariable String rut) {
        log.info("Petición HTTP recibida: GET /api/clientes/rut/{}", rut);

        ClienteResponseDTO cliente = clienteService.obtenerPorRut(rut);

        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("Petición HTTP recibida: GET /api/clientes/{}", id);

        ClienteResponseDTO cliente = clienteService.obtenerPorId(id);

        return ResponseEntity.ok(cliente);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        log.info("Petición HTTP recibida: GET /api/clientes para listar todos los registros.");

        List<ClienteResponseDTO> clientes = clienteService.obtenerTodos();

        return ResponseEntity.ok(clientes);
    }
}