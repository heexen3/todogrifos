package com.todogrifos.despachosms.service;

import com.todogrifos.despachosms.dto.DespachoCreateDTO;
import com.todogrifos.despachosms.dto.DespachoResponseDTO;
import com.todogrifos.despachosms.dto.DespachoStatusUpdateDTO;
import com.todogrifos.despachosms.exception.DespachoDuplicadoException;
import com.todogrifos.despachosms.exception.DespachoNotFoundException;
import com.todogrifos.despachosms.model.Despacho;
import com.todogrifos.despachosms.model.EstadoDespacho;
import com.todogrifos.despachosms.repository.DespachoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DespachoServiceImplTest {

    @Mock
    private DespachoRepository despachoRepository;

    @InjectMocks
    private DespachoServiceImpl despachoService;

    private Despacho despachoMock;
    private DespachoCreateDTO createDtoMock;

    @BeforeEach
    void setUp() {
        despachoMock = Despacho.builder()
                .id(1L)
                .codigoSeguimiento("TRK-100200")
                .ventaId(50L)
                .direccionEnvio("Av. Providencia 1234")
                .comuna("Providencia")
                .estado(EstadoDespacho.PENDIENTE)
                .fechaEntregaEstimada(LocalDateTime.now().plusDays(3))
                .build();

        createDtoMock = DespachoCreateDTO.builder()
                .codigoSeguimiento("TRK-100200")
                .ventaId(50L)
                .direccionEnvio("Av. Providencia 1234")
                .comuna("Providencia")
                .build();
    }

    @Test
    void crearDespacho_cuandoDatosSonNuevos_deberiaRetornarDespachoResponseDTO() {
        // Arrange
        when(despachoRepository.existsByCodigoSeguimiento(anyString())).thenReturn(false);
        when(despachoRepository.existsByVentaId(anyLong())).thenReturn(false);
        when(despachoRepository.save(any(Despacho.class))).thenReturn(despachoMock);

        // Act
        DespachoResponseDTO resultado = despachoService.crearDespacho(createDtoMock);

        // Assert
        assertNotNull(resultado);
        assertEquals("TRK-100200", resultado.getCodigoSeguimiento());
        assertEquals(EstadoDespacho.PENDIENTE.name(), resultado.getEstado());
        verify(despachoRepository, times(1)).save(any(Despacho.class));
    }

    @Test
    void crearDespacho_cuandoCodigoSeguimientoYaExiste_deberiaLanzarDespachoDuplicadoException() {
        // Arrange
        when(despachoRepository.existsByCodigoSeguimiento(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(DespachoDuplicadoException.class, () -> {
            despachoService.crearDespacho(createDtoMock);
        });
        verify(despachoRepository, never()).save(any(Despacho.class));
    }

    @Test
    void actualizarEstado_cuandoDespachoNoExiste_deberiaLanzarDespachoNotFoundException() {
        // Arrange
        when(despachoRepository.findById(99L)).thenReturn(Optional.empty());
        DespachoStatusUpdateDTO updateDto = new DespachoStatusUpdateDTO();
        updateDto.setEstado("EN_RUTA");

        // Act & Assert
        assertThrows(DespachoNotFoundException.class, () -> {
            despachoService.actualizarEstado(99L, updateDto);
        });
        verify(despachoRepository, never()).save(any(Despacho.class));
    }
}
