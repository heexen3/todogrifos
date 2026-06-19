package com.todogrifos.clientesms.service;

import com.todogrifos.clientesms.dto.ClienteCreateDTO;
import com.todogrifos.clientesms.dto.ClienteResponseDTO;
import com.todogrifos.clientesms.exception.ClienteDuplicadoException;
import com.todogrifos.clientesms.exception.ClienteNotFoundException;
import com.todogrifos.clientesms.model.Cliente;
import com.todogrifos.clientesms.repository.ClienteRepository;
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
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository; // Simulamos la base de datos

    @InjectMocks
    private ClienteServiceImpl clienteService; // Inyectamos el mock al servicio real

    private Cliente clienteMock;
    private ClienteCreateDTO createDtoMock;

    @BeforeEach
    void setUp() {
        // Preparamos datos de prueba limpios antes de cada test
        clienteMock = Cliente.builder()
                .id(1L)
                .rut("12345678-9")
                .nombres("Juan")
                .appaterno("Pérez")
                .apmaterno("Soto")
                .email("juan@correo.com")
                .build();

        createDtoMock = ClienteCreateDTO.builder()
                .rut("12345678-9")
                .nombres("Juan")
                .appaterno("Pérez")
                .apmaterno("Soto")
                .email("juan@correo.com")
                .build();
    }

    @Test
    void registrarCliente_cuandoDatosSonNuevos_deberiaRetornarClienteResponseDTO() {
        // Arrange (Preparación)
        when(clienteRepository.existsByRut(anyString())).thenReturn(false);
        when(clienteRepository.existsByEmail(anyString())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteMock);

        // Act (Ejecución)
        ClienteResponseDTO resultado = clienteService.registrarCliente(createDtoMock);

        // Assert (Verificación)
        assertNotNull(resultado);
        assertEquals("Juan Pérez Soto", resultado.getNombreCompleto());
        assertEquals("12345678-9", resultado.getRut());
        verify(clienteRepository, times(1)).save(any(Cliente.class)); // Verificamos que se llamó al save
    }

    @Test
    void registrarCliente_cuandoRutYaExiste_deberiaLanzarClienteDuplicadoException() {
        // Arrange
        when(clienteRepository.existsByRut(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(ClienteDuplicadoException.class, () -> {
            clienteService.registrarCliente(createDtoMock);
        });
        verify(clienteRepository, never()).save(any(Cliente.class)); // Verificamos que NUNCA intentó guardar
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaLanzarClienteNotFoundException() {
        // Arrange
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClienteNotFoundException.class, () -> {
            clienteService.obtenerPorId(99L);
        });
    }
}