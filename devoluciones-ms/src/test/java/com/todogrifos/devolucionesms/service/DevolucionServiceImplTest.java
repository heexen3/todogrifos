package com.todogrifos.devolucionesms.service;

import com.todogrifos.devolucionesms.client.InventarioClient;
import com.todogrifos.devolucionesms.client.VentaClient;
import com.todogrifos.devolucionesms.dto.DevolucionCreateDTO;
import com.todogrifos.devolucionesms.dto.DevolucionResponseDTO;
import com.todogrifos.devolucionesms.exception.DevolucionInvalidaException;
import com.todogrifos.devolucionesms.exception.FolioDuplicadoException;
import com.todogrifos.devolucionesms.model.DestinoDevolucion;
import com.todogrifos.devolucionesms.model.Devolucion;
import com.todogrifos.devolucionesms.repository.DevolucionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DevolucionServiceImplTest {

    @Mock
    private DevolucionRepository devolucionRepository;

    @Mock
    private VentaClient ventaClient;

    @Mock
    private InventarioClient inventarioClient;

    @InjectMocks
    private DevolucionServiceImpl devolucionService;

    private Devolucion devolucionMock;
    private DevolucionCreateDTO createDtoMock;

    @BeforeEach
    void setUp() {
        devolucionMock = Devolucion.builder()
                .id(1L)
                .notaCreditoFolio("NCG-2026-0001")
                .ventaId(50L)
                .sku("SKU-GRIFO-01")
                .cantidad(2)
                .motivo("Defecto de fábrica")
                .destinoLogistico(DestinoDevolucion.REINGRESADO_A_STOCK)
                .build();

        createDtoMock = DevolucionCreateDTO.builder()
                .notaCreditoFolio("NCG-2026-0001")
                .ventaId(50L)
                .sku("SKU-GRIFO-01")
                .cantidad(2)
                .motivo("Defecto de fábrica")
                .destinoLogistico("REINGRESADO_A_STOCK")
                .build();
    }

    @Test
    void procesarDevolucion_cuandoDatosSonValidosYReingresaStock_deberiaRetornarDevolucionResponseDTO() {
        // Arrange
        when(devolucionRepository.existsByNotaCreditoFolio(anyString())).thenReturn(false);
        when(ventaClient.verificarVentaPorId(anyLong())).thenReturn(ResponseEntity.ok(new HashMap<>()));
        when(inventarioClient.adicionarStock(anyString(), any())).thenReturn(ResponseEntity.ok(new HashMap<>()));
        when(devolucionRepository.save(any(Devolucion.class))).thenReturn(devolucionMock);

        // Act
        DevolucionResponseDTO resultado = devolucionService.procesarDevolucion(createDtoMock);

        // Assert
        assertNotNull(resultado);
        assertEquals("NCG-2026-0001", resultado.getNotaCreditoFolio());
        assertEquals("REINGRESADO_A_STOCK", resultado.getDestinoLogistico());
        verify(devolucionRepository, times(1)).save(any(Devolucion.class));
        verify(ventaClient, times(1)).verificarVentaPorId(50L);
        verify(inventarioClient, times(1)).adicionarStock(eq("SKU-GRIFO-01"), any());
    }

    @Test
    void procesarDevolucion_cuandoFolioDuplicado_deberiaLanzarFolioDuplicadoException() {
        // Arrange
        when(devolucionRepository.existsByNotaCreditoFolio(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(FolioDuplicadoException.class, () -> {
            devolucionService.procesarDevolucion(createDtoMock);
        });
        verify(devolucionRepository, never()).save(any(Devolucion.class));
        verify(ventaClient, never()).verificarVentaPorId(anyLong());
        verify(inventarioClient, never()).adicionarStock(anyString(), any());
    }

    @Test
    void procesarDevolucion_cuandoVentaNoExiste_deberiaLanzarDevolucionInvalidaException() {
        // Arrange
        when(devolucionRepository.existsByNotaCreditoFolio(anyString())).thenReturn(false);
        when(ventaClient.verificarVentaPorId(anyLong())).thenThrow(new RuntimeException("Venta no encontrada"));

        // Act & Assert
        assertThrows(DevolucionInvalidaException.class, () -> {
            devolucionService.procesarDevolucion(createDtoMock);
        });
        verify(devolucionRepository, never()).save(any(Devolucion.class));
        verify(inventarioClient, never()).adicionarStock(anyString(), any());
    }
}
