package com.chn.prestamos.service;

import com.chn.prestamos.dto.*;
import com.chn.prestamos.entity.*;
import com.chn.prestamos.mapper.SolicitudPrestamoMapper;
import com.chn.prestamos.repository.ClienteRepository;
import com.chn.prestamos.repository.PrestamoRepository;
import com.chn.prestamos.repository.SolicitudPrestamoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolicitudPrestamoService {

    private final SolicitudPrestamoRepository solicitudPrestamoRepository;
    private final PrestamoRepository prestamoRepository;
    private final ClienteRepository clienteRepository;

    public SolicitudPrestamoResponse crear(SolicitudPrestamoRequest request) {

        Cliente cliente = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        SolicitudPrestamo solicitud = SolicitudPrestamo.builder()
                .cliente(cliente)
                .montoSolicitado(request.getMontoSolicitado())
                .plazoMeses(request.getPlazoMeses())
                .build();

        return SolicitudPrestamoMapper.toResponse(solicitudPrestamoRepository.save(solicitud));
    }

    public List<SolicitudPrestamoResponse> listarPorCliente(Integer idCliente) {
        return solicitudPrestamoRepository.findByClienteIdCliente(idCliente)
                .stream()
                .map(SolicitudPrestamoMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<SolicitudPrestamoResponse> listarTodos() {
        return solicitudPrestamoRepository.findByEstado(EstadoSolicitud.PENDIENTE)
                .stream()
                .map(SolicitudPrestamoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public SolicitudPrestamoResponse aprobar(Integer idSolicitud, String comentario) {

        SolicitudPrestamo solicitud = solicitudPrestamoRepository.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (!EstadoSolicitud.PENDIENTE.equals(solicitud.getEstado())) {
            throw new RuntimeException("Solo solicitudes pendientes pueden aprobarse");
        }

        solicitud.setEstado(EstadoSolicitud.APROBADO);
        solicitud.setFechaResolucion(LocalDateTime.now());
        solicitud.setComentarioResolucion(comentario);

        SolicitudPrestamoResponse respuesta = SolicitudPrestamoMapper.toResponse(solicitudPrestamoRepository.save(solicitud));

        crearPrestamoAprobado(solicitud);

        return respuesta;
    }

    public SolicitudPrestamoResponse rechazar(Integer idSolicitud, String comentario) {

        SolicitudPrestamo solicitud = solicitudPrestamoRepository.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (!EstadoSolicitud.PENDIENTE.equals(solicitud.getEstado())) {
            throw new RuntimeException("Solo solicitudes pendientes pueden rechazarse");
        }

        solicitud.setEstado(EstadoSolicitud.RECHAZADO);
        solicitud.setFechaResolucion(LocalDateTime.now());
        solicitud.setComentarioResolucion(comentario);

        return SolicitudPrestamoMapper.toResponse(solicitudPrestamoRepository.save(solicitud));
    }

    private void crearPrestamoAprobado(SolicitudPrestamo solicitud){
        if (prestamoRepository.existsByIdSolicitud(solicitud)) {
            throw new RuntimeException("La solicitud ya tiene un préstamo generado");
        }

        Prestamo prestamo = Prestamo.builder()
                .cliente(solicitud.getCliente())
                .idSolicitud(solicitud)
                .montoAprobado(solicitud.getMontoSolicitado())
                .tasaInteres(BigDecimal.valueOf(0.12))
                .saldoPendiente(solicitud.getMontoSolicitado())
                .build();

        prestamoRepository.save(prestamo);
    }
}