package com.todogrifos.vendedoresms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todogrifos.vendedoresms.dto.VendedorCreateDTO;
import com.todogrifos.vendedoresms.dto.VendedorResponseDTO;
import com.todogrifos.vendedoresms.service.VendedorService;
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

@WebMvcTest(VendedorController.class)
class VendedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VendedorService vendedorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void obtenerTodos_deberiaRetornarStatus200YListaDeVendedores() throws Exception {
        // Arrange
        VendedorResponseDTO vend = VendedorResponseDTO.builder()
                .id(1L)
                .codigoInterno("VEND-001")
                .rut("12.345.678-9")
                .nombre("Diego Portales")
                .sucursal("Santiago Centro")
                .porcentajeComision(5.0)
                .comisionAcumulada(15000.0)
                .build();
        when(vendedorService.obtenerTodos()).thenReturn(List.of(vend));

        // Act & Assert
        mockMvc.perform(get("/api/vendedores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigoInterno").value("VEND-001"))
                .andExpect(jsonPath("$[0].rut").value("12.345.678-9"))
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void crearVendedor_cuandoDatosSonValidos_deberiaRetornarStatus201YVendedor() throws Exception {
        // Arrange
        VendedorCreateDTO request = VendedorCreateDTO.builder()
                .codigoInterno("VEND-001")
                .rut("12.345.678-9")
                .nombre("Diego Portales")
                .sucursal("Santiago Centro")
                .porcentajeComision(5.0)
                .build();

        VendedorResponseDTO response = VendedorResponseDTO.builder()
                .id(1L)
                .codigoInterno("VEND-001")
                .rut("12.345.678-9")
                .nombre("Diego Portales")
                .sucursal("Santiago Centro")
                .porcentajeComision(5.0)
                .comisionAcumulada(0.0)
                .build();

        when(vendedorService.crearVendedor(any(VendedorCreateDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/vendedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigoInterno").value("VEND-001"))
                .andExpect(jsonPath("$.rut").value("12.345.678-9"));
    }
}
