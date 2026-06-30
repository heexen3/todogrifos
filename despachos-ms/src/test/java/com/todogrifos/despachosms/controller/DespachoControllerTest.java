package com.todogrifos.despachosms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todogrifos.despachosms.dto.DespachoCreateDTO;
import com.todogrifos.despachosms.dto.DespachoResponseDTO;
import com.todogrifos.despachosms.model.EstadoDespacho;
import com.todogrifos.despachosms.service.DespachoService;
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

@WebMvcTest(DespachoController.class)
class DespachoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DespachoService despachoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void obtenerTodos_deberiaRetornarStatus200YListaDeDespachos() throws Exception {
        // Arrange
        DespachoResponseDTO despacho = DespachoResponseDTO.builder()
                .id(1L)
                .codigoSeguimiento("TRK-100200")
                .ventaId(50L)
                .direccionEnvio("Av. Providencia 1234")
                .comuna("Providencia")
                .estado("PENDIENTE")
                .fechaEntregaEstimada(LocalDateTime.now().plusDays(3))
                .build();
        when(despachoService.obtenerTodos()).thenReturn(List.of(despacho));

        // Act & Assert
        mockMvc.perform(get("/api/despachos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigoSeguimiento").value("TRK-100200"))
                .andExpect(jsonPath("$[0].comuna").value("Providencia"))
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void crearDespacho_cuandoDatosSonValidos_deberiaRetornarStatus201YDespacho() throws Exception {
        // Arrange
        DespachoCreateDTO request = DespachoCreateDTO.builder()
                .codigoSeguimiento("TRK-100200")
                .ventaId(50L)
                .direccionEnvio("Av. Providencia 1234")
                .comuna("Providencia")
                .build();

        DespachoResponseDTO response = DespachoResponseDTO.builder()
                .id(1L)
                .codigoSeguimiento("TRK-100200")
                .ventaId(50L)
                .direccionEnvio("Av. Providencia 1234")
                .comuna("Providencia")
                .estado("PENDIENTE")
                .fechaEntregaEstimada(LocalDateTime.now().plusDays(3))
                .build();

        when(despachoService.crearDespacho(any(DespachoCreateDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/despachos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigoSeguimiento").value("TRK-100200"))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }
}
