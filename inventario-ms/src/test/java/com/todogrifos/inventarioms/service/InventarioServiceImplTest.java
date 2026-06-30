package com.todogrifos.inventarioms.service;

import com.todogrifos.inventarioms.client.ProductoClient;
import com.todogrifos.inventarioms.dto.InventarioCreateDTO;
import com.todogrifos.inventarioms.dto.InventarioResponseDTO;
import com.todogrifos.inventarioms.dto.ProductoDTO;
import com.todogrifos.inventarioms.dto.StockUpdateDTO;
import com.todogrifos.inventarioms.exception.InvalidStockQuantityException;
import com.todogrifos.inventarioms.exception.InventarioNotFoundException;
import com.todogrifos.inventarioms.exception.SkuDuplicadoException;
import com.todogrifos.inventarioms.model.Inventario;
import com.todogrifos.inventarioms.repository.InventarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceImplTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private ProductoClient productoClient;

    @InjectMocks
    private InventarioServiceImpl inventarioService;

    private Inventario inventarioMock;
    private InventarioCreateDTO createDtoMock;
    private ProductoDTO productoMock;

    @BeforeEach
    void setUp() {
        inventarioMock = Inventario.builder()
                .id(1L)
                .sku("SKU-GRIFO-01")
                .cantidad(25)
                .build();

        createDtoMock = InventarioCreateDTO.builder()
                .sku("SKU-GRIFO-01")
                .cantidad(25)
                .build();

        productoMock = new ProductoDTO(10L, "Grifo Elegante", "SKU-GRIFO-01", 15000.0);
    }

    @Test
    void crearInventario_cuandoDatosValidos_deberiaRetornarInventarioResponseDTO() {
        // Arrange
        when(productoClient.getProductoBySku(anyString())).thenReturn(productoMock);
        when(inventarioRepository.existsBySku(anyString())).thenReturn(false);
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventarioMock);

        // Act
        InventarioResponseDTO resultado = inventarioService.crearInventario(createDtoMock);

        // Assert
        assertNotNull(resultado);
        assertEquals("SKU-GRIFO-01", resultado.getSku());
        assertEquals(25, resultado.getCantidad());
        verify(inventarioRepository, times(1)).save(any(Inventario.class));
    }

    @Test
    void crearInventario_cuandoSkuDuplicado_deberiaLanzarSkuDuplicadoException() {
        // Arrange
        when(productoClient.getProductoBySku(anyString())).thenReturn(productoMock);
        when(inventarioRepository.existsBySku(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(SkuDuplicadoException.class, () -> {
            inventarioService.crearInventario(createDtoMock);
        });
        verify(inventarioRepository, never()).save(any(Inventario.class));
    }

    @Test
    void obtenerPorSku_cuandoNoExiste_deberiaLanzarInventarioNotFoundException() {
        // Arrange
        when(inventarioRepository.findBySku("NON-EXISTENT")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InventarioNotFoundException.class, () -> {
            inventarioService.obtenerPorSku("NON-EXISTENT");
        });
    }
}
