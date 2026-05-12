package com.todogrifos.productosms.controller;


import com.todogrifos.productosms.dto.ProductoCreateDTO;
import com.todogrifos.productosms.dto.ProductoDTO;
import com.todogrifos.productosms.dto.ProductoUpdateDTO;
import com.todogrifos.productosms.model.Producto;
import com.todogrifos.productosms.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping
    public ResponseEntity<ProductoDTO> registrarProducto(@Valid @RequestBody ProductoCreateDTO dto) {
        ProductoDTO nuevoProductoDTO = productoService.registrarProducto(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(nuevoProductoDTO);
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listarTodos() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerProductoPorId(@PathVariable Long id) {
        ProductoDTO productoDTO = productoService.obtenerProductoPorId(id);
        return ResponseEntity.ok(productoDTO);
    }

    // Búsqueda general (para filtrar muchos resultados)
    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoDTO>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productoService.buscarPorNombre(nombre));
    }

    // Búsqueda concreta por SKU (identificador unico comercial)
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductoDTO> buscarPorSku(@PathVariable String sku) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productoService.buscarPorSku(sku));
    }

    @GetMapping("/marca/{id}")
    public ResponseEntity<List<ProductoDTO>> listarPorMarca(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productoService.listarPorMarca(id));
    }

    @GetMapping("/categoria/{id}")
    public ResponseEntity<List<ProductoDTO>> listarPorCategoria(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productoService.listarPorCategoria(id));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ProductoDTO>> listarActivos() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productoService.listarProductosActivos());
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoUpdateDTO dto) {
        ProductoDTO productoDTOActualizado = productoService.actualizarProducto(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(productoDTOActualizado);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {

        productoService.borrarProducto(id);

        return ResponseEntity.noContent().build();
    }

}
