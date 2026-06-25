package com.todogrifos.productosms.controller;


import com.todogrifos.productosms.dto.ProductoCreateDTO;
import com.todogrifos.productosms.dto.ProductoDTO;
import com.todogrifos.productosms.dto.ProductoUpdateDTO;
import com.todogrifos.productosms.model.Producto;
import com.todogrifos.productosms.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Catálogo de productos, gestión de stock base, marcas, categorías y búsqueda de productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping
    @Operation(
            summary = "Registrar un nuevo producto",
            description = "Permite crear un producto dentro del catálogo, asociándolo a una marca y categoría."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
            @ApiResponse(responseCode = "409", description = "Conflicto: SKU o producto ya existente"),
            @ApiResponse(responseCode = "500", description = "Error interno al registrar el producto")
    })
    public ResponseEntity<ProductoDTO> registrarProducto(@Valid @RequestBody ProductoCreateDTO dto) {
        ProductoDTO nuevoProductoDTO = productoService.registrarProducto(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(nuevoProductoDTO);
    }

    @GetMapping
    @Operation(
            summary = "Listar todos los productos",
            description = "Retorna el catálogo completo de productos registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de productos obtenido exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno al recuperar los productos")
    })
    public ResponseEntity<List<ProductoDTO>> listarTodos() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener producto por ID",
            description = "Permite consultar un producto específico utilizando su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno al consultar el producto")
    })
    public ResponseEntity<ProductoDTO> obtenerProductoPorId(@PathVariable Long id) {
        ProductoDTO productoDTO = productoService.obtenerProductoPorId(id);
        return ResponseEntity.ok(productoDTO);
    }

    // Búsqueda general (para filtrar muchos resultados)
    @GetMapping("/buscar")
    @Operation(
            summary = "Buscar productos por nombre",
            description = "Permite filtrar productos del catálogo utilizando coincidencia parcial o completa por nombre."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetro de búsqueda inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno al realizar la búsqueda")
    })
    public ResponseEntity<List<ProductoDTO>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productoService.buscarPorNombre(nombre));
    }


    // Búsqueda concreta por SKU (identificador unico comercial)
    @GetMapping("/sku/{sku}")
    @Operation(
            summary = "Buscar producto por SKU",
            description = "Permite obtener un producto específico utilizando su código SKU único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe un producto con el SKU indicado"),
            @ApiResponse(responseCode = "500", description = "Error interno al realizar la búsqueda")
    })
    public ResponseEntity<ProductoDTO> buscarPorSku(@PathVariable String sku) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productoService.buscarPorSku(sku));
    }

    @GetMapping("/marca/{id}")
    @Operation(
            summary = "Listar productos por marca",
            description = "Permite obtener todos los productos asociados a una marca específica mediante su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de productos por marca obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "No existen productos para la marca indicada"),
            @ApiResponse(responseCode = "500", description = "Error interno al consultar productos por marca")
    })
    public ResponseEntity<List<ProductoDTO>> listarPorMarca(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productoService.listarPorMarca(id));
    }

    @GetMapping("/categoria/{id}")
    @Operation(
            summary = "Listar productos por categoría",
            description = "Permite obtener todos los productos asociados a una categoría específica mediante su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de productos por categoría obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "No existen productos para la categoría indicada"),
            @ApiResponse(responseCode = "500", description = "Error interno al consultar productos por categoría")
    })
    public ResponseEntity<List<ProductoDTO>> listarPorCategoria(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productoService.listarPorCategoria(id));
    }

    @GetMapping("/activos")
    @Operation(
            summary = "Listar productos activos",
            description = "Retorna todos los productos que se encuentran activos dentro del catálogo."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de productos activos obtenido correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno al consultar productos activos")
    })
    public ResponseEntity<List<ProductoDTO>> listarActivos() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productoService.listarProductosActivos());
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar producto",
            description = "Permite modificar la información de un producto existente en el catálogo."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o inconsistentes"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno al actualizar el producto")
    })
    public ResponseEntity<ProductoDTO> actualizarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoUpdateDTO dto) {
        ProductoDTO productoDTOActualizado = productoService.actualizarProducto(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(productoDTOActualizado);
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar producto",
            description = "Permite eliminar un producto del catálogo utilizando su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno al eliminar el producto")
    })
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {

        productoService.borrarProducto(id);

        return ResponseEntity.noContent().build();
    }

}
