package com.todogrifos.vendedoresms.service;

import com.todogrifos.vendedoresms.dto.VendedorCreateDTO;
import com.todogrifos.vendedoresms.dto.VendedorResponseDTO;
import com.todogrifos.vendedoresms.exception.VendedorDuplicadoException;
import com.todogrifos.vendedoresms.exception.VendedorNotFoundException;
import com.todogrifos.vendedoresms.model.Vendedor;
import com.todogrifos.vendedoresms.repository.VendedorRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendedorServiceImplTest {

    @Mock
    private VendedorRepository vendedorRepository;

    @InjectMocks
    private VendedorServiceImpl vendedorService;

    private Vendedor vendedorMock;
    private VendedorCreateDTO createDtoMock;

    @BeforeEach
    void setUp() {
        vendedorMock = Vendedor.builder()
                .id(1L)
                .codigoInterno("VEND-001")
                .rut("12.345.678-9")
                .nombre("Diego Portales")
                .sucursal("Santiago Centro")
                .porcentajeComision(5.0)
                .comisionAcumulada(0.0)
                .build();

        createDtoMock = VendedorCreateDTO.builder()
                .codigoInterno("VEND-001")
                .rut("12.345.678-9")
                .nombre("Diego Portales")
                .sucursal("Santiago Centro")
                .porcentajeComision(5.0)
                .build();
    }

    @Test
    void crearVendedor_cuandoDatosSonNuevos_deberiaRetornarVendedorResponseDTO() {
        // Arrange
        when(vendedorRepository.existsByCodigoInterno(anyString())).thenReturn(false);
        when(vendedorRepository.existsByRut(anyString())).thenReturn(false);
        when(vendedorRepository.save(any(Vendedor.class))).thenReturn(vendedorMock);

        // Act
        VendedorResponseDTO resultado = vendedorService.crearVendedor(createDtoMock);

        // Assert
        assertNotNull(resultado);
        assertEquals("VEND-001", resultado.getCodigoInterno());
        assertEquals("12.345.678-9", resultado.getRut());
        verify(vendedorRepository, times(1)).save(any(Vendedor.class));
    }

    @Test
    void crearVendedor_cuandoCodigoInternoYaExiste_deberiaLanzarVendedorDuplicadoException() {
        // Arrange
        when(vendedorRepository.existsByCodigoInterno(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(VendedorDuplicadoException.class, () -> {
            vendedorService.crearVendedor(createDtoMock);
        });
        verify(vendedorRepository, never()).save(any(Vendedor.class));
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaLanzarVendedorNotFoundException() {
        // Arrange
        when(vendedorRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(VendedorNotFoundException.class, () -> {
            vendedorService.obtenerPorId(99L);
        });
    }
}
