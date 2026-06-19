package com.todogrifos.clientesms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todogrifos.clientesms.dto.ClienteCreateDTO;
import com.todogrifos.clientesms.dto.ClienteResponseDTO;
import com.todogrifos.clientesms.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class) // Levanta solo el contexto web del controller
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc; // Permite simular peticiones HTTP reales

    @MockBean
    private ClienteService clienteService; // Simulamos el servicio para no tocar la BD

    @Autowired
    private ObjectMapper objectMapper; // Para convertir DTOs a JSON

    @Test
    void listarTodos_deberiaRetornarStatus200YListaDeClientes() throws Exception {
        // Arrange
        ClienteResponseDTO cliente = ClienteResponseDTO.builder()
                .id(1L)
                .rut("12345678-9")
                .nombreCompleto("Juan Pérez")
                .build();
        when(clienteService.obtenerTodos()).thenReturn(List.of(cliente));

        // Act & Assert
        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk()) // Esperamos un 200 OK
                .andExpect(jsonPath("$[0].rut").value("12345678-9")) // El JSON debe traer el RUT
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void registrarCliente_cuandoDatosSonValidos_deberiaRetornarStatus201YCliente() throws Exception {
        // Arrange
        ClienteCreateDTO request = ClienteCreateDTO.builder()
                .rut("12345678-9")
                .nombres("Juan")
                .appaterno("Pérez")
                .apmaterno("Soto")
                .email("juan@correo.com")
                .build();

        ClienteResponseDTO response = ClienteResponseDTO.builder()
                .id(1L)
                .rut("12345678-9")
                .nombreCompleto("Juan Pérez")
                .build();

        when(clienteService.registrarCliente(any(ClienteCreateDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))) // Convertimos el DTO a String JSON
                .andExpect(status().isCreated()) // Esperamos un 201 Created
                .andExpect(jsonPath("$.nombreCompleto").value("Juan Pérez"));
    }
}