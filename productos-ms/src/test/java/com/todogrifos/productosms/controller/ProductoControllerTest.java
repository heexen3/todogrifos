package com.todogrifos.productosms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todogrifos.productosms.dto.ProductoCreateDTO;
import com.todogrifos.productosms.dto.ProductoDTO;
import com.todogrifos.productosms.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarTodos_deberiaRetornarStatus200YListaDeProductos() throws Exception {
        // Arrange
        ProductoDTO prod = new ProductoDTO(
                1L,
                "SKU-GRIFO-01",
                "Grifo Elegante",
                "Grifo cromado",
                BigDecimal.valueOf(15000.0),
                true,
                12,
                "Groz",
                "Grifería"
        );
        when(productoService.listarProductos()).thenReturn(List.of(prod));

        // Act & Assert
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("SKU-GRIFO-01"))
                .andExpect(jsonPath("$[0].nombre").value("Grifo Elegante"))
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void registrarProducto_cuandoDatosSonValidos_deberiaRetornarStatus201YProducto() throws Exception {
        // Arrange
        ProductoCreateDTO request = new ProductoCreateDTO();
        request.setSku("SKU-GRIFO-01");
        request.setNombre("Grifo Elegante");
        request.setDescripcion("Grifo cromado");
        request.setPrecio(BigDecimal.valueOf(15000.0));
        request.setGarantiaMeses(12);
        request.setMarcaId(2L);
        request.setCategoriaId(3L);

        ProductoDTO response = new ProductoDTO(
                1L,
                "SKU-GRIFO-01",
                "Grifo Elegante",
                "Grifo cromado",
                BigDecimal.valueOf(15000.0),
                true,
                12,
                "Groz",
                "Grifería"
        );

        when(productoService.registrarProducto(any(ProductoCreateDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sku").value("SKU-GRIFO-01"))
                .andExpect(jsonPath("$.nombre").value("Grifo Elegante"));
    }
}
