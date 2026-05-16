package com.todogrifos.ventasms.service;

import com.todogrifos.ventasms.client.ClienteClient;
import com.todogrifos.ventasms.client.InventarioClient;
import com.todogrifos.ventasms.dto.VentaCreateDTO;
import com.todogrifos.ventasms.dto.VentaDetalleCreateDTO;
import com.todogrifos.ventasms.dto.VentaDetalleResponseDTO;
import com.todogrifos.ventasms.dto.VentaResponseDTO;
import com.todogrifos.ventasms.exception.FolioDuplicadoException;
import com.todogrifos.ventasms.exception.VentaInvalidaException;
import com.todogrifos.ventasms.model.Venta;
import com.todogrifos.ventasms.model.VentaDetalle;
import com.todogrifos.ventasms.repository.VentaRepository;
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
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteClient clienteClient;

    @Autowired
    private InventarioClient inventarioClient;

    @Override
    @Transactional // asegura rollback
    public VentaResponseDTO procesarVenta(VentaCreateDTO dto) {
        log.info("Iniciando transaccion comercial para el folio de boleta: {}", dto.getFolio());

        // validar folio unico
        if (ventaRepository.existsByFolio(dto.getFolio())) {
            log.warn("Fallo transaccional: El folio {} ya fue emitido previamente.", dto.getFolio());
            throw new FolioDuplicadoException("El número de folio '" + dto.getFolio() + "' ya existe registrado en el sistema.");
        }

        // validar cliente de forma remota
        try {
            log.info("Estableciendo comunicacion con clientes-ms para validar ID: {}", dto.getClienteId());
            clienteClient.obtenerPorId(dto.getClienteId());
        } catch (FeignException.NotFound e) {
            log.warn("Validacion remota fallida: El cliente con ID {} no existe.", dto.getClienteId());
            throw new VentaInvalidaException("No se puede procesar la venta. El cliente provisto no existe en el sistema maestro.");
        } catch (FeignException e) {
            log.error("Error de infraestructura de red al conectar con clientes-ms: {}", e.getMessage());
            throw new VentaInvalidaException("Servicio de clientes no disponible temporalmente. Intente más tarde.");
        }

        // crear cabecera del modelo Maestro
        Venta venta = Venta.builder()
                .folio(dto.getFolio())
                .fechaVenta(LocalDateTime.now())
                .clienteId(dto.getClienteId())
                .total(0.0) // se calculará de forma dinámica sumando los subtotales
                .build();

        double totalAcumulado = 0.0;

        // recorrer el carrito de compras, validar y descontar inventarios mediante OpenFeign
        for (VentaDetalleCreateDTO detalleDto : dto.getDetalles()) {

            // modificar el stock remotamente
            try {
                log.info("Estableciendo comunicacion con inventario-ms. Solicitando retiro de {} unidades del SKU: {}",
                        detalleDto.getCantidad(), detalleDto.getSku());

                Map<String, Object> cuerpoRequest = new HashMap<>();

                cuerpoRequest.put("cantidadModificada", detalleDto.getCantidad());

                inventarioClient.retirarStock(detalleDto.getSku(), cuerpoRequest);

            } catch (FeignException.BadRequest e) {
                log.warn("Validacion de stock fallida: Existencias insuficientes para el SKU: {}", detalleDto.getSku());
                throw new VentaInvalidaException("Stock insuficiente o invalido en bodega para el artículo con SKU: " + detalleDto.getSku());
            } catch (FeignException.NotFound e) {
                log.warn("Validacion de inventario fallida: El SKU {} no existe en bodega.", detalleDto.getSku());
                throw new VentaInvalidaException("El producto con SKU '" + detalleDto.getSku() + "' no se encuentra registrado en el inventario.");
            } catch (FeignException e) {
                log.error("Fallo crítico de infraestructura de red al conectar con inventario-ms: {}", e.getMessage());
                throw new VentaInvalidaException("El sistema de inventarios no responde. Venta cancelada para proteger consistencia.");
            }

            double subtotalItem = detalleDto.getCantidad() * detalleDto.getPrecioUnitario();
            totalAcumulado += subtotalItem;

            // construir el modelo del Detalle asociándolo al Maestro
            VentaDetalle detalleEntity = VentaDetalle.builder()
                    .sku(detalleDto.getSku())
                    .cantidad(detalleDto.getCantidad())
                    .precioUnitario(detalleDto.getPrecioUnitario())
                    .subtotal(subtotalItem)
                    .build();

            venta.agregarDetalle(detalleEntity);
        }

        venta.setTotal(totalAcumulado);

        // persistir en cascada
        Venta ventaGuardada = ventaRepository.save(venta);
        log.info("Venta procesada y boleta guardada con éxito. ID: {}, Folio: {}, Total: ${}",
                ventaGuardada.getId(), ventaGuardada.getFolio(), ventaGuardada.getTotal());

        return mapToResponseDTO(ventaGuardada);
    }

    @Override
    public List<VentaResponseDTO> obtenerTodas() {
        log.info("Recuperando listado historico de boletas emitidas.");
        return ventaRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private VentaResponseDTO mapToResponseDTO(Venta venta) {
        List<VentaDetalleResponseDTO> detallesDto = venta.getDetalles().stream()
                .map(d -> VentaDetalleResponseDTO.builder()
                        .id(d.getId())
                        .sku(d.getSku())
                        .cantidad(d.getCantidad())
                        .precioUnitario(d.getPrecioUnitario())
                        .subtotal(d.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        return VentaResponseDTO.builder()
                .id(venta.getId())
                .folio(venta.getFolio())
                .fechaVenta(venta.getFechaVenta())
                .clienteId(venta.getClienteId())
                .total(venta.getTotal())
                .detalles(detallesDto)
                .build();
    }
}