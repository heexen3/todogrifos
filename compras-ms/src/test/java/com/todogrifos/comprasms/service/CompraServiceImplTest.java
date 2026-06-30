package com.todogrifos.comprasms.service;

import com.todogrifos.comprasms.client.InventarioClient;
import com.todogrifos.comprasms.dto.CompraCreateDTO;
import com.todogrifos.comprasms.dto.CompraDetalleCreateDTO;
import com.todogrifos.comprasms.dto.CompraResponseDTO;
import com.todogrifos.comprasms.exception.FacturaDuplicadaException;
import com.todogrifos.comprasms.model.Compra;
import com.todogrifos.comprasms.repository.CompraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompraServiceImplTest {

    @Mock
    private CompraRepository compraRepository;

    @Mock
    private InventarioClient inventarioClient;

    @InjectMocks
    private CompraServiceImpl compraService;

    private Compra compraMock;
    private CompraCreateDTO createDtoMock;

    @BeforeEach
    void setUp() {
        compraMock = Compra.builder()
                .id(1L)
                .numeroFactura("FAC-1001")
                .fechaCompra(LocalDateTime.now())
                .proveedor("Proveedor Grifos SpA")
                .total(15000.0)
                .detalles(new ArrayList<>())
                .build();

        CompraDetalleCreateDTO detalleDto = CompraDetalleCreateDTO.builder()
                .sku("SKU-GRIFO-01")
                .cantidad(5)
                .precioCosto(3000.0)
                .build();

        createDtoMock = CompraCreateDTO.builder()
                .numeroFactura("FAC-1001")
                .proveedor("Proveedor Grifos SpA")
                .detalles(List.of(detalleDto))
                .build();
    }

    @Test
    void procesarCompra_cuandoDatosSonNuevos_deberiaRetornarCompraResponseDTO() {
        // Arrange
        when(compraRepository.existsByNumeroFactura(anyString())).thenReturn(false);
        when(inventarioClient.agregarStock(anyString(), any())).thenReturn(ResponseEntity.ok(new HashMap<>()));
        when(compraRepository.save(any(Compra.class))).thenReturn(compraMock);

        // Act
        CompraResponseDTO resultado = compraService.procesarCompra(createDtoMock);

        // Assert
        assertNotNull(resultado);
        assertEquals("FAC-1001", resultado.getNumeroFactura());
        assertEquals("Proveedor Grifos SpA", resultado.getProveedor());
        verify(compraRepository, times(1)).save(any(Compra.class));
        verify(inventarioClient, times(1)).agregarStock(eq("SKU-GRIFO-01"), any());
    }

    @Test
    void procesarCompra_cuandoFacturaYaExiste_deberiaLanzarFacturaDuplicadaException() {
        // Arrange
        when(compraRepository.existsByNumeroFactura(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(FacturaDuplicadaException.class, () -> {
            compraService.procesarCompra(createDtoMock);
        });
        verify(compraRepository, never()).save(any(Compra.class));
        verify(inventarioClient, never()).agregarStock(anyString(), any());
    }

    @Test
    void obtenerTodas_deberiaRetornarListaDeCompras() {
        // Arrange
        when(compraRepository.findAll()).thenReturn(List.of(compraMock));

        // Act
        List<CompraResponseDTO> resultado = compraService.obtenerTodas();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("FAC-1001", resultado.get(0).getNumeroFactura());
    }
}
