package com.todogrifos.devolucionesms.service;

import com.todogrifos.devolucionesms.client.InventarioClient;
import com.todogrifos.devolucionesms.client.VentaClient;
import com.todogrifos.devolucionesms.dto.DevolucionCreateDTO;
import com.todogrifos.devolucionesms.dto.DevolucionResponseDTO;
import com.todogrifos.devolucionesms.exception.DevolucionInvalidaException;
import com.todogrifos.devolucionesms.exception.FolioDuplicadoException;
import com.todogrifos.devolucionesms.model.DestinoDevolucion;
import com.todogrifos.devolucionesms.model.Devolucion;
import com.todogrifos.devolucionesms.repository.DevolucionRepository;
import com.todogrifos.devolucionesms.service.DevolucionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DevolucionServiceImpl implements DevolucionService {

    @Autowired
    private DevolucionRepository DevolucionRepository;

    @Autowired
    private VentaClient ventaClient;

    @Autowired
    private InventarioClient inventarioClient;

    @Override
    @Transactional
    public DevolucionResponseDTO procesarDevolucion(DevolucionCreateDTO dto) {
        log.info("Iniciando auditoría de post-venta. Folio Nota de Crédito solicitado: {}, Venta Origen ID: {}",
                dto.getNotaCreditoFolio(), dto.getVentaId());

        // Validar unicidad del Folio de la Nota de Crédito
        if (DevolucionRepository.existsByNotaCreditoFolio(dto.getNotaCreditoFolio())) {
            log.warn("Rechazo logístico: El folio tributario {} ya fue emitido previamente.", dto.getNotaCreditoFolio());
            throw new FolioDuplicadoException("La Nota de Crédito con folio '" + dto.getNotaCreditoFolio() + "' ya existe registrada.");
        }

        // VALIDACIÓN REMOTA (OpenFeign): Verificar si la venta existe en ventas-ms
        try {
            log.info("Llamando vía Feign Client a ventas-ms para validar la boleta ID: {}", dto.getVentaId());
            ventaClient.verificarVentaPorId(dto.getVentaId());
        } catch (Exception e) {
            log.error("Fallo distribuido: No se pudo verificar la boleta ID {} o no existe en ventas-ms. Detalles: {}",
                    dto.getVentaId(), e.getMessage());
            throw new DevolucionInvalidaException("No se puede emitir la devolución. La venta origen ID " + dto.getVentaId() + " es inexistente o inválida.");
        }

        // Parsear el string validado al tipo Enum real
        DestinoDevolucion destino = DestinoDevolucion.valueOf(dto.getDestinoLogistico());

        // Si vuelve a stock comercial, adicionamos mercadería en bodega (OpenFeign)
        if (destino == DestinoDevolucion.REINGRESADO_A_STOCK) {
            try {
                log.info("Llamando vía Feign Client a inventario-ms para reingresar {} unidades al SKU: {}",
                        dto.getCantidad(), dto.getSku());

                Map<String, Object> stockRequest = new HashMap<>();
                stockRequest.put("cantidad", dto.getCantidad());

                inventarioClient.adicionarStock(dto.getSku(), stockRequest);
                log.info("Stock reincorporado con éxito en el almacén virtual.");
            } catch (Exception e) {
                log.error("Fallo crítico distribuido al actualizar stock para SKU {}. Operación cancelada. Error: {}",
                        dto.getSku(), e.getMessage());
                throw new DevolucionInvalidaException("Error de comunicación remota: No se pudo actualizar el inventario físico para el SKU " + dto.getSku());
            }
        } else {
            // Si el destino es ENVIADO_A_MERMA, se registra localmente para auditoría de mermas pero no se incrementa stock
            log.warn("Alerta logística: El producto SKU {} con cantidad {} fue descartado. Motivo: '{}'. Se desvía a MERMA.",
                    dto.getSku(), dto.getCantidad(), dto.getMotivo());
        }

        // Persistir localmente la bitácora de la Devolución mediante Builder
        Devolucion devolucion = Devolucion.builder()
                .notaCreditoFolio(dto.getNotaCreditoFolio())
                .ventaId(dto.getVentaId())
                .sku(dto.getSku())
                .cantidad(dto.getCantidad())
                .motivo(dto.getMotivo())
                .destinoLogistico(destino)
                .build();

        Devolucion guardada = DevolucionRepository.save(devolucion);
        log.info("Nota de Crédito '{}' guardada exitosamente con ID local: {}.", guardada.getNotaCreditoFolio(), guardada.getId());

        return mapToResponseDTO(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DevolucionResponseDTO> obtenerTodas() {
        log.info("Consultando el histórico completo de Notas de Crédito emitidas.");
        return DevolucionRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DevolucionResponseDTO> obtenerPorVentaId(Long ventaId) {
        log.info("Buscando Notas de Crédito asociadas al ID de Venta: {}", ventaId);
        return DevolucionRepository.findByVentaId(ventaId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private DevolucionResponseDTO mapToResponseDTO(Devolucion devolucion) {
        return DevolucionResponseDTO.builder()
                .id(devolucion.getId())
                .notaCreditoFolio(devolucion.getNotaCreditoFolio())
                .ventaId(devolucion.getVentaId())
                .sku(devolucion.getSku())
                .cantidad(devolucion.getCantidad())
                .motivo(devolucion.getMotivo())
                .destinoLogistico(devolucion.getDestinoLogistico().name())
                .fechaDevolucion(devolucion.getFechaDevolucion())
                .build();
    }
}