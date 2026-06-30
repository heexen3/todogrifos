package com.todogrifos.ventasms.service;

import com.todogrifos.ventasms.client.ClienteClient;
import com.todogrifos.ventasms.client.InventarioClient;
import com.todogrifos.ventasms.dto.VentaCreateDTO;
import com.todogrifos.ventasms.dto.VentaDetalleCreateDTO;
import com.todogrifos.ventasms.dto.VentaResponseDTO;
import com.todogrifos.ventasms.exception.FolioDuplicadoException;
import com.todogrifos.ventasms.exception.VentaInvalidaException;
import com.todogrifos.ventasms.model.Venta;
import com.todogrifos.ventasms.repository.VentaRepository;
import feign.FeignException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentaServiceImplTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private ClienteClient clienteClient;

    @Mock
    private InventarioClient inventarioClient;

    @InjectMocks
    private VentaServiceImpl ventaService;

    private Venta ventaMock;
    private VentaCreateDTO createDtoMock;

    @BeforeEach
    void setUp() {
        ventaMock = Venta.builder()
                .id(1L)
                .folio("BOL-100200")
                .fechaVenta(LocalDateTime.now())
                .clienteId(5L)
                .total(20000.0)
                .detalles(new ArrayList<>())
                .build();

        VentaDetalleCreateDTO detalleDto = VentaDetalleCreateDTO.builder()
                .sku("SKU-GRIFO-01")
                .cantidad(2)
                .precioUnitario(10000.0)
                .build();

        createDtoMock = VentaCreateDTO.builder()
                .folio("BOL-100200")
                .clienteId(5L)
                .detalles(List.of(detalleDto))
                .build();
    }

    @Test
    void procesarVenta_cuandoDatosSonValidos_deberiaRetornarVentaResponseDTO() {
        // Arrange
        when(ventaRepository.existsByFolio(anyString())).thenReturn(false);
        when(clienteClient.obtenerPorId(anyLong())).thenReturn(ResponseEntity.ok(new HashMap<>()));
        when(inventarioClient.retirarStock(anyString(), any())).thenReturn(ResponseEntity.ok(new HashMap<>()));
        when(ventaRepository.save(any(Venta.class))).thenReturn(ventaMock);

        // Act
        VentaResponseDTO resultado = ventaService.procesarVenta(createDtoMock);

        // Assert
        assertNotNull(resultado);
        assertEquals("BOL-100200", resultado.getFolio());
        assertEquals(20000.0, resultado.getTotal());
        verify(ventaRepository, times(1)).save(any(Venta.class));
        verify(clienteClient, times(1)).obtenerPorId(5L);
        verify(inventarioClient, times(1)).retirarStock(eq("SKU-GRIFO-01"), any());
    }

    @Test
    void procesarVenta_cuandoFolioYaExiste_deberiaLanzarFolioDuplicadoException() {
        // Arrange
        when(ventaRepository.existsByFolio(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(FolioDuplicadoException.class, () -> {
            ventaService.procesarVenta(createDtoMock);
        });
        verify(ventaRepository, never()).save(any(Venta.class));
        verify(clienteClient, never()).obtenerPorId(anyLong());
        verify(inventarioClient, never()).retirarStock(anyString(), any());
    }

    @Test
    void procesarVenta_cuandoClienteNoExiste_deberiaLanzarVentaInvalidaException() {
        // Arrange
        when(ventaRepository.existsByFolio(anyString())).thenReturn(false);
        
        // Mock Feign NotFound Exception
        FeignException.NotFound notFoundEx = mock(FeignException.NotFound.class);
        when(clienteClient.obtenerPorId(anyLong())).thenThrow(notFoundEx);

        // Act & Assert
        assertThrows(VentaInvalidaException.class, () -> {
            ventaService.procesarVenta(createDtoMock);
        });
        verify(ventaRepository, never()).save(any(Venta.class));
        verify(inventarioClient, never()).retirarStock(anyString(), any());
    }
}
