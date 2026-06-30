package com.todogrifos.productosms.service;

import com.todogrifos.productosms.dto.ProductoCreateDTO;
import com.todogrifos.productosms.dto.ProductoDTO;
import com.todogrifos.productosms.exception.ProductoNotFoundException;
import com.todogrifos.productosms.exception.SkuDuplicadoException;
import com.todogrifos.productosms.model.Categoria;
import com.todogrifos.productosms.model.Marca;
import com.todogrifos.productosms.model.Producto;
import com.todogrifos.productosms.repository.CategoriaRepository;
import com.todogrifos.productosms.repository.MarcaRepository;
import com.todogrifos.productosms.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private MarcaRepository marcaRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoService productoService;

    private Marca marcaMock;
    private Categoria categoriaMock;
    private Producto productoMock;
    private ProductoCreateDTO createDtoMock;

    @BeforeEach
    void setUp() {
        marcaMock = new Marca(2L, "Groz", "Alemania");
        categoriaMock = new Categoria(3L, "Grifería", "Grifos y llaves de paso", null, null, null);
        productoMock = new Producto(
                1L,
                "SKU-GRIFO-01",
                "Grifo Elegante",
                "Grifo cromado",
                BigDecimal.valueOf(15000.0),
                true,
                12,
                marcaMock,
                categoriaMock
        );

        createDtoMock = new ProductoCreateDTO();
        createDtoMock.setSku("SKU-GRIFO-01");
        createDtoMock.setNombre("Grifo Elegante");
        createDtoMock.setDescripcion("Grifo cromado");
        createDtoMock.setPrecio(BigDecimal.valueOf(15000.0));
        createDtoMock.setGarantiaMeses(12);
        createDtoMock.setMarcaId(2L);
        createDtoMock.setCategoriaId(3L);
    }

    @Test
    void registrarProducto_cuandoDatosSonNuevos_deberiaRetornarProductoDTO() {
        // Arrange
        when(productoRepository.existsBySku(anyString())).thenReturn(false);
        when(marcaRepository.findById(anyLong())).thenReturn(Optional.of(marcaMock));
        when(categoriaRepository.findById(anyLong())).thenReturn(Optional.of(categoriaMock));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoMock);

        // Act
        ProductoDTO resultado = productoService.registrarProducto(createDtoMock);

        // Assert
        assertNotNull(resultado);
        assertEquals("SKU-GRIFO-01", resultado.getSku());
        assertEquals("Grifo Elegante", resultado.getNombre());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void registrarProducto_cuandoSkuYaExiste_deberiaLanzarSkuDuplicadoException() {
        // Arrange
        when(productoRepository.existsBySku(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(SkuDuplicadoException.class, () -> {
            productoService.registrarProducto(createDtoMock);
        });
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void obtenerProductoPorId_cuandoNoExiste_deberiaLanzarProductoNotFoundException() {
        // Arrange
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductoNotFoundException.class, () -> {
            productoService.obtenerProductoPorId(99L);
        });
    }
}
