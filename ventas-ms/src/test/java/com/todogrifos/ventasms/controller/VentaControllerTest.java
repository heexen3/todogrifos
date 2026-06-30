package com.todogrifos.ventasms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todogrifos.ventasms.dto.VentaCreateDTO;
import com.todogrifos.ventasms.dto.VentaDetalleCreateDTO;
import com.todogrifos.ventasms.dto.VentaResponseDTO;
import com.todogrifos.ventasms.service.VentaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VentaController.class)
class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VentaService ventaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarTodas_deberiaRetornarStatus200YListaDeVentas() throws Exception {
        // Arrange
        VentaResponseDTO venta = VentaResponseDTO.builder()
                .id(1L)
                .folio("BOL-100200")
                .clienteId(5L)
                .total(20000.0)
                .fechaVenta(LocalDateTime.now())
                .detalles(List.of())
                .build();
        when(ventaService.obtenerTodas()).thenReturn(List.of(venta));

        // Act & Assert
        mockMvc.perform(get("/api/ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].folio").value("BOL-100200"))
                .andExpect(jsonPath("$[0].total").value(20000.0))
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void procesarVenta_cuandoDatosSonValidos_deberiaRetornarStatus201YVenta() throws Exception {
        // Arrange
        VentaDetalleCreateDTO detalle = VentaDetalleCreateDTO.builder()
                .sku("SKU-GRIFO-01")
                .cantidad(2)
                .precioUnitario(10000.0)
                .build();

        VentaCreateDTO request = VentaCreateDTO.builder()
                .folio("BOL-100200")
                .clienteId(5L)
                .vendedorId(2L)
                .detalles(List.of(detalle))
                .build();

        VentaResponseDTO response = VentaResponseDTO.builder()
                .id(1L)
                .folio("BOL-100200")
                .clienteId(5L)
                .total(20000.0)
                .fechaVenta(LocalDateTime.now())
                .detalles(List.of())
                .build();

        when(ventaService.procesarVenta(any(VentaCreateDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.folio").value("BOL-100200"))
                .andExpect(jsonPath("$.total").value(20000.0));
    }
}
