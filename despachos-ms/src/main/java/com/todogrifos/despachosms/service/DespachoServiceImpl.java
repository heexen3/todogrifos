package com.todogrifos.despachosms.service.impl;

import com.todogrifos.despachosms.dto.DespachoCreateDTO;
import com.todogrifos.despachosms.dto.DespachoResponseDTO;
import com.todogrifos.despachosms.dto.DespachoStatusUpdateDTO;
import com.todogrifos.despachosms.exception.DespachoDuplicadoException;
import com.todogrifos.despachosms.exception.DespachoNotFoundException;
import com.todogrifos.despachosms.model.Despacho;
import com.todogrifos.despachosms.model.EstadoDespacho;
import com.todogrifos.despachosms.repository.DespachoRepository;
import com.todogrifos.despachosms.service.DespachoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DespachoServiceImpl implements DespachoService {

    @Autowired
    private DespachoRepository despachoRepository;

    @Override
    @Transactional
    public DespachoResponseDTO crearDespacho(DespachoCreateDTO dto) {
        log.info("Iniciando generación de orden de despacho para la Venta ID: {}. Tracking solicitado: {}",
                dto.getVentaId(), dto.getCodigoSeguimiento());

        // Validar unicidad del código de seguimiento
        if (despachoRepository.existsByCodigoSeguimiento(dto.getCodigoSeguimiento())) {
            log.warn("Rechazo de operación: El código de seguimiento '{}' ya existe.", dto.getCodigoSeguimiento());
            throw new DespachoDuplicadoException("El código de seguimiento '" + dto.getCodigoSeguimiento() + "' ya está asignado a otra entrega.");
        }

        // Validar que la venta no posea un despacho previo asignado
        if (despachoRepository.existsByVentaId(dto.getVentaId())) {
            log.warn("Rechazo de operación: La Venta ID {} ya cuenta con una orden logística activa.", dto.getVentaId());
            throw new DespachoDuplicadoException("La venta con ID " + dto.getVentaId() + " ya tiene un despacho registrado.");
        }

        // calcular la fecha estimada de entrega (lógica comercial: FechaActual + 3 días)
        LocalDateTime entregaEstimada = LocalDateTime.now().plusDays(3);

        Despacho despacho = Despacho.builder()
                .codigoSeguimiento(dto.getCodigoSeguimiento())
                .ventaId(dto.getVentaId())
                .direccionEnvio(dto.getDireccionEnvio())
                .comuna(dto.getComuna())
                .estado(EstadoDespacho.PENDIENTE) // Nace estrictamente en PENDIENTE
                .fechaEntregaEstimada(entregaEstimada)
                .build();

        Despacho guardado = despachoRepository.save(despacho);
        log.info("Orden logística guardada con éxito. ID asignado: {}, Comuna destino: {}, Fecha estimada: {}",
                guardado.getId(), guardado.getComuna(), guardado.getFechaEntregaEstimada());

        return mapToResponseDTO(guardado);
    }

    @Override
    @Transactional
    public DespachoResponseDTO actualizarEstado(Long id, DespachoStatusUpdateDTO dto) {
        log.info("Solicitud de actualización de tránsito para el Despacho ID: {}. Nuevo Estado: {}", id, dto.getEstado());

        Despacho despacho = despachoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Fallo de actualización: No existe despacho con ID: {}", id);
                    return new DespachoNotFoundException("No se encontró ninguna orden logística con el ID: " + id);
                });

        // Parsear de forma segura el String validado del DTO al Tipo Enum nativo
        EstadoDespacho nuevoEstado = EstadoDespacho.valueOf(dto.getEstado());

        // Registrar traza del cambio de estado
        log.info("Transición lícita en máquina de estados para ID {}: [{}] -> [{}]",
                despacho.getId(), despacho.getEstado(), nuevoEstado);

        despacho.setEstado(nuevoEstado);

        // Si ya fue entregado en terreno, actualizamos la fecha real de cierre al momento actual
        if (nuevoEstado == EstadoDespacho.ENTREGADO) {
            despacho.setFechaEntregaEstimada(LocalDateTime.now());
            log.info("Despacho ID {} marcado como finalizado. Entrega efectuada.", despacho.getId());
        }

        Despacho actualizado = despachoRepository.save(despacho);
        return mapToResponseDTO(actualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DespachoResponseDTO> obtenerTodos() {
        log.info("Recuperando histórico consolidado de despachos en el sistema.");
        return despachoRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DespachoResponseDTO obtenerPorTracking(String codigoSeguimiento) {
        log.info("Rastreando orden por código de seguimiento: {}", codigoSeguimiento);
        Despacho despacho = despachoRepository.findByCodigoSeguimiento(codigoSeguimiento)
                .orElseThrow(() -> {
                    log.warn("Rastreo fallido: Tracking '{}' inexistente.", codigoSeguimiento);
                    return new DespachoNotFoundException("No se registra ningún envío con el código de seguimiento: " + codigoSeguimiento);
                });
        return mapToResponseDTO(despacho);
    }

    @Override
    @Transactional(readOnly = true)
    public DespachoResponseDTO obtenerPorVentaId(Long ventaId) {
        log.info("Consultando estado de despacho para la boleta ID: {}", ventaId);
        Despacho despacho = despachoRepository.findByVentaId(ventaId)
                .orElseThrow(() -> {
                    log.warn("Consulta fallida: La boleta ID {} no posee orden de envío registrada.", ventaId);
                    return new DespachoNotFoundException("No se encontró despacho asociado a la venta ID: " + ventaId);
                });
        return mapToResponseDTO(despacho);
    }

    private DespachoResponseDTO mapToResponseDTO(Despacho despacho) {
        return DespachoResponseDTO.builder()
                .id(despacho.getId())
                .codigoSeguimiento(despacho.getCodigoSeguimiento())
                .ventaId(despacho.getVentaId())
                .direccionEnvio(despacho.getDireccionEnvio())
                .comuna(despacho.getComuna())
                .estado(despacho.getEstado().name())
                .fechaCreacion(despacho.getFechaCreacion())
                .fechaEntregaEstimada(despacho.getFechaEntregaEstimada())
                .build();
    }
}