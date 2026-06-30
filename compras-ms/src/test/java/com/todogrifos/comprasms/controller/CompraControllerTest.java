package com.todogrifos.comprasms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todogrifos.comprasms.dto.CompraCreateDTO;
import com.todogrifos.comprasms.dto.CompraDetalleCreateDTO;
import com.todogrifos.comprasms.dto.CompraResponseDTO;
import com.todogrifos.comprasms.service.CompraService;
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

@WebMvcTest(CompraController.class)
class CompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompraService compraService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarTodas_deberiaRetornarStatus200YListaDeCompras() throws Exception {
        // Arrange
        CompraResponseDTO compra = CompraResponseDTO.builder()
                .id(1L)
                .numeroFactura("FAC-1001")
                .proveedor("Proveedor Grifos SpA")
                .total(50000.0)
                .fechaCompra(LocalDateTime.now())
                .build();
        when(compraService.obtenerTodas()).thenReturn(List.of(compra));

        // Act & Assert
        mockMvc.perform(get("/api/compras"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroFactura").value("FAC-1001"))
                .andExpect(jsonPath("$[0].proveedor").value("Proveedor Grifos SpA"))
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void registrarCompra_cuandoDatosSonValidos_deberiaRetornarStatus201YCompra() throws Exception {
        // Arrange
        CompraDetalleCreateDTO detalle = CompraDetalleCreateDTO.builder()
                .sku("SKU-GRIFO-01")
                .cantidad(5)
                .precioCosto(3000.0)
                .build();

        CompraCreateDTO request = CompraCreateDTO.builder()
                .numeroFactura("FAC-1001")
                .proveedor("Proveedor Grifos SpA")
                .detalles(List.of(detalle))
                .build();

        CompraResponseDTO response = CompraResponseDTO.builder()
                .id(1L)
                .numeroFactura("FAC-1001")
                .proveedor("Proveedor Grifos SpA")
                .total(50000.0)
                .fechaCompra(LocalDateTime.now())
                .build();

        when(compraService.procesarCompra(any(CompraCreateDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/compras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroFactura").value("FAC-1001"))
                .andExpect(jsonPath("$.proveedor").value("Proveedor Grifos SpA"));
    }
}
