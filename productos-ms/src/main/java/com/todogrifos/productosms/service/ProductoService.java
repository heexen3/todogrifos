package com.todogrifos.productosms.service;

import com.todogrifos.productosms.dto.ProductoCreateDTO;
import com.todogrifos.productosms.dto.ProductoDTO;
import com.todogrifos.productosms.dto.ProductoUpdateDTO;
import com.todogrifos.productosms.exception.CategoriaNotFoundException;
import com.todogrifos.productosms.exception.MarcaNotFoundException;
import com.todogrifos.productosms.exception.ProductoNotFoundException;
import com.todogrifos.productosms.exception.SkuDuplicadoException;
import com.todogrifos.productosms.model.Producto;
import com.todogrifos.productosms.repository.CategoriaRepository;
import com.todogrifos.productosms.repository.MarcaRepository;
import com.todogrifos.productosms.repository.ProductoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private MarcaRepository marcaRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    // POST
    public ProductoDTO registrarProducto(ProductoCreateDTO dto) {
        log.info("Iniciando registro de producto con SKU: {}", dto.getSku());
        if (productoRepository.existsBySku(dto.getSku())) {
            log.error("Fallo al registrar: El SKU {} ya está en uso", dto.getSku());
            throw new SkuDuplicadoException("El SKU " + dto.getSku() + " ya está registrado.");
        }
        Producto producto = convertirAEntidad(dto);
        producto.setActivo(true);

        producto.setMarca(marcaRepository.findById(dto.getMarcaId())
                .orElseThrow(() -> {
                    log.warn("Error en registro: Marca ID {} no existe", dto.getMarcaId());
                    return new MarcaNotFoundException("Marca no encontrada");
                }));

        producto.setCategoria(categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> {
                    log.warn("Error en registro: Categoría ID {} no existe", dto.getCategoriaId());
                    return new CategoriaNotFoundException("Categoría no encontrada");
                }));

        Producto guardado = productoRepository.save(producto);
        log.info("Producto registrado exitosamente con ID: {}", guardado.getId());
        return convertirADTO(guardado);
    }

    // GET
    public ProductoDTO obtenerProductoPorId(Long id) {
        log.info("Solicitud de búsqueda para producto ID: {}", id);
        return productoRepository.findById(id)
                .map(p -> {
                    log.debug("Producto recuperado: {}", p.getNombre());
                    return convertirADTO(p);
                })
                .orElseThrow(() -> {
                    log.warn("Producto no encontrado con ID: {}", id);
                    return new ProductoNotFoundException("No existe producto con ID " + id);
                });
    }

    public List<ProductoDTO> listarProductos() {
        log.info("Listando todos los productos del catálogo");
        return productoRepository.findAll()
                .stream().map(this::convertirADTO).toList();
    }

    // PUT
    public ProductoDTO actualizarProducto(Long id, ProductoUpdateDTO dto) {
        log.info("Actualizando producto ID: {}", id);
        Producto existente = productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Fallo al actualizar: Producto ID {} no encontrado", id);
                    return new ProductoNotFoundException("Producto no encontrado.");
                });

        existente.setNombre(dto.getNombre());
        existente.setActivo(dto.getActivo());
        existente.setPrecio(dto.getPrecio());
        existente.setGarantiaMeses(dto.getGarantiaMeses());
        existente.setDescripcion(dto.getDescripcion());

        Producto actualizado = productoRepository.save(existente);
        log.info("Producto ID {} actualizado correctamente", id);
        return convertirADTO(actualizado);
    }

    // DELETE
    public void borrarProducto(Long id) {
        log.info("Iniciando eliminación de producto ID: {}", id);
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Fallo al eliminar: Producto ID {} no existe", id);
                    return new ProductoNotFoundException("Producto no encontrado.");
                });
        productoRepository.delete(producto);
        log.info("Producto ID {} eliminado exitosamente", id);
    }

    public List<ProductoDTO> buscarPorNombre(String nombre) {
        log.info("Búsqueda de productos por nombre: '{}'", nombre);
        return productoRepository
                .findByNombreContainingIgnoreCase(nombre).stream().map(this::convertirADTO).toList();
    }

    public ProductoDTO buscarPorSku(String sku) {
        log.info("Búsqueda de producto por SKU: {}", sku);
        return productoRepository.findBySku(sku).map(this::convertirADTO).orElseThrow(() -> {
            log.warn("SKU {} no encontrado", sku);
            return new ProductoNotFoundException("Producto no encontrado.");
        });
    }

    public List<ProductoDTO> listarPorMarca(Long marcaId) {
        log.info("Filtrando productos por marca ID: {}", marcaId);
        return productoRepository.findByMarca_Id(marcaId).stream().map(this::convertirADTO).toList();
    }

    public List<ProductoDTO> listarPorCategoria(Long categoriaId) {
        log.info("Filtrando productos por categoría ID: {}", categoriaId);
        return productoRepository.findByCategoria_Id(categoriaId).stream().map(this::convertirADTO).toList();
    }

    public List<ProductoDTO> listarProductosActivos() {
        log.info("Recuperando lista de productos activos");
        return productoRepository.findByActivoTrue().stream().map(this::convertirADTO).toList();
    }

    private ProductoDTO convertirADTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setSku(producto.getSku());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setActivo(producto.getActivo());
        dto.setGarantiaMeses(producto.getGarantiaMeses());
        dto.setMarca(producto.getMarca().getNombre());
        dto.setCategoria(producto.getCategoria().getNombre());
        return dto;
    }

    private Producto convertirAEntidad(ProductoCreateDTO dto) {
        Producto producto = new Producto();
        producto.setSku(dto.getSku());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setActivo(dto.getActivo());
        producto.setGarantiaMeses(dto.getGarantiaMeses());
        return producto;
    }
}