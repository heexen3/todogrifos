package com.todogrifos.proveedoresms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todogrifos.proveedoresms.dto.ProveedorCreateDTO;
import com.todogrifos.proveedoresms.dto.ProveedorResponseDTO;
import com.todogrifos.proveedoresms.service.ProveedorService;
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

@WebMvcTest(ProveedorController.class)
class ProveedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProveedorService proveedorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void obtenerTodos_deberiaRetornarStatus200YListaDeProveedores() throws Exception {
        // Arrange
        ProveedorResponseDTO prov = ProveedorResponseDTO.builder()
                .id(1L)
                .rut("76.123.456-K")
                .razonSocial("Proveedor Grifos SpA")
                .email("contacto@grifos.cl")
                .telefono("+56912345678")
                .direccion("Av. Lo Espejo 100")
                .build();
        when(proveedorService.obtenerTodos()).thenReturn(List.of(prov));

        // Act & Assert
        mockMvc.perform(get("/api/proveedores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rut").value("76.123.456-K"))
                .andExpect(jsonPath("$[0].razonSocial").value("Proveedor Grifos SpA"))
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void registrarProveedor_cuandoDatosSonValidos_deberiaRetornarStatus201YProveedor() throws Exception {
        // Arrange
        ProveedorCreateDTO request = ProveedorCreateDTO.builder()
                .rut("76.123.456-K")
                .razonSocial("Proveedor Grifos SpA")
                .email("contacto@grifos.cl")
                .telefono("+56912345678")
                .direccion("Av. Lo Espejo 100")
                .build();

        ProveedorResponseDTO response = ProveedorResponseDTO.builder()
                .id(1L)
                .rut("76.123.456-K")
                .razonSocial("Proveedor Grifos SpA")
                .email("contacto@grifos.cl")
                .telefono("+56912345678")
                .direccion("Av. Lo Espejo 100")
                .build();

        when(proveedorService.crearProveedor(any(ProveedorCreateDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/proveedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rut").value("76.123.456-K"))
                .andExpect(jsonPath("$.razonSocial").value("Proveedor Grifos SpA"));
    }
}
