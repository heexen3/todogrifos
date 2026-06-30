package com.todogrifos.devolucionesms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todogrifos.devolucionesms.dto.DevolucionCreateDTO;
import com.todogrifos.devolucionesms.dto.DevolucionResponseDTO;
import com.todogrifos.devolucionesms.model.DestinoDevolucion;
import com.todogrifos.devolucionesms.service.DevolucionService;
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

@WebMvcTest(DevolucionController.class)
class DevolucionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DevolucionService devolucionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void obtenerTodas_deberiaRetornarStatus200YListaDeDevoluciones() throws Exception {
        // Arrange
        DevolucionResponseDTO devolucion = DevolucionResponseDTO.builder()
                .id(1L)
                .notaCreditoFolio("NCG-2026-0001")
                .ventaId(50L)
                .sku("SKU-GRIFO-01")
                .cantidad(2)
                .motivo("Defecto de fábrica")
                .destinoLogistico("REINGRESADO_A_STOCK")
                .build();
        when(devolucionService.obtenerTodas()).thenReturn(List.of(devolucion));

        // Act & Assert
        mockMvc.perform(get("/api/devoluciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].notaCreditoFolio").value("NCG-2026-0001"))
                .andExpect(jsonPath("$[0].sku").value("SKU-GRIFO-01"))
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void procesarDevolucion_cuandoDatosSonValidos_deberiaRetornarStatus201YDevolucion() throws Exception {
        // Arrange
        DevolucionCreateDTO request = DevolucionCreateDTO.builder()
                .notaCreditoFolio("NCG-2026-0001")
                .ventaId(50L)
                .sku("SKU-GRIFO-01")
                .cantidad(2)
                .motivo("Defecto de fábrica")
                .destinoLogistico("REINGRESADO_A_STOCK")
                .build();

        DevolucionResponseDTO response = DevolucionResponseDTO.builder()
                .id(1L)
                .notaCreditoFolio("NCG-2026-0001")
                .ventaId(50L)
                .sku("SKU-GRIFO-01")
                .cantidad(2)
                .motivo("Defecto de fábrica")
                .destinoLogistico("REINGRESADO_A_STOCK")
                .build();

        when(devolucionService.procesarDevolucion(any(DevolucionCreateDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/devoluciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.notaCreditoFolio").value("NCG-2026-0001"))
                .andExpect(jsonPath("$.sku").value("SKU-GRIFO-01"));
    }
}
