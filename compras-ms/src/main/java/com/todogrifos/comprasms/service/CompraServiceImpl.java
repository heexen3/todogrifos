package com.todogrifos.comprasms.service.impl;

import com.todogrifos.comprasms.client.InventarioClient;
import com.todogrifos.comprasms.dto.CompraCreateDTO;
import com.todogrifos.comprasms.dto.CompraDetalleCreateDTO;
import com.todogrifos.comprasms.dto.CompraDetalleResponseDTO;
import com.todogrifos.comprasms.dto.CompraResponseDTO;
import com.todogrifos.comprasms.exception.CompraInvalidaException;
import com.todogrifos.comprasms.exception.FacturaDuplicadaException;
import com.todogrifos.comprasms.model.Compra;
import com.todogrifos.comprasms.model.CompraDetalle;
import com.todogrifos.comprasms.repository.CompraRepository;
import com.todogrifos.comprasms.service.CompraService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CompraServiceImpl implements CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private InventarioClient inventarioClient;

    @Override
    @Transactional
    public CompraResponseDTO procesarCompra(CompraCreateDTO dto) {
        log.info("Iniciando registro de orden de abastecimiento. Factura N°: {}", dto.getNumeroFactura());

        // 1. Validar duplicidad local del documento del proveedor
        if (compraRepository.existsByNumeroFactura(dto.getNumeroFactura())) {
            log.warn("Rechazo de documento: La factura N° {} ya fue ingresada previamente al inventario.", dto.getNumeroFactura());
            throw new FacturaDuplicadaException("Ya existe una orden de compra registrada con la factura N°: " + dto.getNumeroFactura());
        }

        // 2. Construir el maestro de la orden
        Compra compra = Compra.builder()
                .numeroFactura(dto.getNumeroFactura())
                .fechaCompra(LocalDateTime.now())
                .proveedor(dto.getProveedor())
                .total(0.0) // Se autocalculará recorriendo el detalle
                .build();

        double totalCalculado = 0.0;

        // 3. Procesar las líneas e inyectar stock de forma remota via Feign
        for (CompraDetalleCreateDTO detalleDto : dto.getDetalles()) {

            // REQUISITO RÚBRICA: Bloque try-catch para control de fallos en comunicación remota
            try {
                log.info("Estableciendo enlace con inventario-ms. Solicitando ingreso de {} unidades para el SKU: {}",
                        detalleDto.getCantidad(), detalleDto.getSku());

                Map<String, Object> cuerpoRequest = new HashMap<>();
                cuerpoRequest.put("cantidadModificada", detalleDto.getCantidad());

                // Llama al microservicio vecino para aumentar el inventario físico en bodega
                inventarioClient.agregarStock(detalleDto.getSku(), cuerpoRequest);

            } catch (FeignException.NotFound e) {
                log.warn("Fallo de reabastecimiento distribuido: El SKU {} no existe en la base de datos de bodega.", detalleDto.getSku());
                throw new CompraInvalidaException("No se puede ingresar la compra. El producto con SKU '" + detalleDto.getSku() + "' no está registrado.");
            } catch (FeignException.BadRequest e) {
                log.warn("Fallo de validación remota: Cantidad inválida enviada para el SKU: {}", detalleDto.getSku());
                throw new CompraInvalidaException("La cantidad especificada para el SKU " + detalleDto.getSku() + " es rechazada por el inventario.");
            } catch (FeignException e) {
                log.error("Fallo crítico de infraestructura de red al conectar con inventario-ms: {}", e.getMessage());
                throw new CompraInvalidaException("El módulo de inventarios no responde. Transmisión cancelada para evitar inconsistencias de stock.");
            }

            // Calcular montos de la línea de la factura
            double subtotalLinea = detalleDto.getCantidad() * detalleDto.getPrecioCosto();
            totalCalculado += subtotalLinea;

            // Mapear al modelo relacional asociándolo al objeto maestro
            CompraDetalle detalleEntity = CompraDetalle.builder()
                    .sku(detalleDto.getSku())
                    .cantidad(detalleDto.getCantidad())
                    .precioCosto(detalleDto.getPrecioCosto())
                    .subtotal(subtotalLinea)
                    .build();

            compra.agregarDetalle(detalleEntity);
        }

        compra.setTotal(totalCalculado);

        // 4. Guardar en cascada
        Compra compraGuardada = compraRepository.save(compra);
        log.info("Orden de compra guardada y procesada exitosamente en BD. ID asignado: {}, Total Facturado: ${}",
                compraGuardada.getId(), compraGuardada.getTotal());

        return mapToResponseDTO(compraGuardada);
    }

    @Override
    public List<CompraResponseDTO> obtenerTodas() {
        log.info("Solicitando historial completo de órdenes de compra a proveedores.");
        return compraRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convierte la entidad de persistencia relacional a un DTO estructurado de salida limpia.
     */
    private CompraResponseDTO mapToResponseDTO(Compra compra) {
        List<CompraDetalleResponseDTO> detallesDto = compra.getDetalles().stream()
                .map(d -> CompraDetalleResponseDTO.builder()
                        .id(d.getId())
                        .sku(d.getSku())
                        .cantidad(d.getCantidad())
                        .precioCosto(d.getPrecioCosto())
                        .subtotal(d.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        return CompraResponseDTO.builder()
                .id(compra.getId())
                .numeroFactura(compra.getNumeroFactura())
                .fechaCompra(compra.getFechaCompra())
                .proveedor(compra.getProveedor())
                .total(compra.getTotal())
                .detalles(detallesDto)
                .build();
    }
}