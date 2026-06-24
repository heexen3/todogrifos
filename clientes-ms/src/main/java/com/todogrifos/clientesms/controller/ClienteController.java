package com.todogrifos.clientesms.controller;

import com.todogrifos.clientesms.dto.ClienteCreateDTO;
import com.todogrifos.clientesms.dto.ClienteResponseDTO;
import com.todogrifos.clientesms.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Clientes", description = "Controlador para el registro y consulta de clientes")
public class ClienteController {
    //hola
    @Autowired
    private ClienteService clienteService;

    @PostMapping
    @Operation(summary = "Registrar un nuevo cliente", description = "Permite ingresar un cliente validando que el RUT no esté duplicado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o RUT duplicado")
    })
    public ResponseEntity<ClienteResponseDTO> registrarCliente(@Valid @RequestBody ClienteCreateDTO dto) {
        log.info("Petición HTTP recibida: POST /api/clientes para registrar el RUT: {}", dto.getRut());
        ClienteResponseDTO nuevoCliente = clienteService.registrarCliente(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    @GetMapping("/rut/{rut}")
    @Operation(summary = "Obtener cliente por su RUT", description = "Busca en los registros un cliente que coincida exactamente con el RUT proporcionado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<ClienteResponseDTO> obtenerPorRut(
            @Parameter(description = "RUT del cliente a buscar (ej: 12345678-9)", required = true)
            @PathVariable String rut) {
        log.info("Petición HTTP recibida: GET /api/clientes/rut/{}", rut);
        ClienteResponseDTO cliente = clienteService.obtenerPorRut(rut);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cliente por ID", description = "Busca un cliente específico utilizando su identificador único autoincremental.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "ID de cliente no existente")
    })
    public ResponseEntity<ClienteResponseDTO> obtenerPorId(
            @Parameter(description = "ID numérico del cliente", required = true)
            @PathVariable Long id) {
        log.info("Petición HTTP recibida: GET /api/clientes/{}", id);
        ClienteResponseDTO cliente = clienteService.obtenerPorId(id);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping
    @Operation(summary = "Listar todos los clientes", description = "Retorna una lista completa con todos los clientes registrados en el sistema.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        log.info("Petición HTTP recibida: GET /api/clientes para listar todos los registros.");
        List<ClienteResponseDTO> clientes = clienteService.obtenerTodos();
        return ResponseEntity.ok(clientes);
    }
}