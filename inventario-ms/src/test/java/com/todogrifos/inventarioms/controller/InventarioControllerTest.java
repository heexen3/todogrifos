package com.todogrifos.inventarioms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todogrifos.inventarioms.dto.InventarioCreateDTO;
import com.todogrifos.inventarioms.dto.InventarioResponseDTO;
import com.todogrifos.inventarioms.service.InventarioService;
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

@WebMvcTest(InventarioController.class)
class InventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventarioService inventarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void obtenerTodos_deberiaRetornarStatus200YListaDeInventarios() throws Exception {
        // Arrange
        InventarioResponseDTO item = InventarioResponseDTO.builder()
                .id(1L)
                .sku("SKU-GRIFO-01")
                .cantidad(25)
                .build();
        when(inventarioService.obtenerTodo()).thenReturn(List.of(item));

        // Act & Assert
        mockMvc.perform(get("/api/inventario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("SKU-GRIFO-01"))
                .andExpect(jsonPath("$[0].cantidad").value(25))
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void crear_cuandoDatosSonValidos_deberiaRetornarStatus201YInventario() throws Exception {
        // Arrange
        InventarioCreateDTO request = InventarioCreateDTO.builder()
                .sku("SKU-GRIFO-01")
                .cantidad(25)
                .build();

        InventarioResponseDTO response = InventarioResponseDTO.builder()
                .id(1L)
                .sku("SKU-GRIFO-01")
                .cantidad(25)
                .build();

        when(inventarioService.crearInventario(any(InventarioCreateDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sku").value("SKU-GRIFO-01"))
                .andExpect(jsonPath("$.cantidad").value(25));
    }
}
