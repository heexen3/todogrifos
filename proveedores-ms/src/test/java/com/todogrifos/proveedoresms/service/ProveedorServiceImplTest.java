package com.todogrifos.proveedoresms.service;

import com.todogrifos.proveedoresms.dto.ProveedorCreateDTO;
import com.todogrifos.proveedoresms.dto.ProveedorResponseDTO;
import com.todogrifos.proveedoresms.exception.ProveedorDuplicadoException;
import com.todogrifos.proveedoresms.exception.ProveedorNotFoundException;
import com.todogrifos.proveedoresms.model.Proveedor;
import com.todogrifos.proveedoresms.repository.ProveedorRepository;
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
class ProveedorServiceImplTest {

    @Mock
    private ProveedorRepository proveedorRepository;

    @InjectMocks
    private ProveedorServiceImpl proveedorService;

    private Proveedor proveedorMock;
    private ProveedorCreateDTO createDtoMock;

    @BeforeEach
    void setUp() {
        proveedorMock = Proveedor.builder()
                .id(1L)
                .rut("76.123.456-K")
                .razonSocial("Proveedor Grifos SpA")
                .email("contacto@grifos.cl")
                .telefono("+56912345678")
                .direccion("Av. Lo Espejo 100")
                .build();

        createDtoMock = ProveedorCreateDTO.builder()
                .rut("76.123.456-K")
                .razonSocial("Proveedor Grifos SpA")
                .email("contacto@grifos.cl")
                .telefono("+56912345678")
                .direccion("Av. Lo Espejo 100")
                .build();
    }

    @Test
    void crearProveedor_cuandoDatosSonNuevos_deberiaRetornarProveedorResponseDTO() {
        // Arrange
        when(proveedorRepository.existsByRut(anyString())).thenReturn(false);
        when(proveedorRepository.existsByEmail(anyString())).thenReturn(false);
        when(proveedorRepository.save(any(Proveedor.class))).thenReturn(proveedorMock);

        // Act
        ProveedorResponseDTO resultado = proveedorService.crearProveedor(createDtoMock);

        // Assert
        assertNotNull(resultado);
        assertEquals("76.123.456-K", resultado.getRut());
        assertEquals("Proveedor Grifos SpA", resultado.getRazonSocial());
        verify(proveedorRepository, times(1)).save(any(Proveedor.class));
    }

    @Test
    void crearProveedor_cuandoRutYaExiste_deberiaLanzarProveedorDuplicadoException() {
        // Arrange
        when(proveedorRepository.existsByRut(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(ProveedorDuplicadoException.class, () -> {
            proveedorService.crearProveedor(createDtoMock);
        });
        verify(proveedorRepository, never()).save(any(Proveedor.class));
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaLanzarProveedorNotFoundException() {
        // Arrange
        when(proveedorRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProveedorNotFoundException.class, () -> {
            proveedorService.obtenerPorId(99L);
        });
    }
}
