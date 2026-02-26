package com.chn.prestamos.mapper;

import com.chn.prestamos.dto.SolicitudPrestamoResponse;
import com.chn.prestamos.entity.SolicitudPrestamo;

public class SolicitudPrestamoMapper {

    public static SolicitudPrestamoResponse toResponse(SolicitudPrestamo solicitud) {
        return SolicitudPrestamoResponse.builder()
                .idSolicitud(solicitud.getIdSolicitud())
                .idCliente(solicitud.getCliente().getIdCliente())
                .montoSolicitado(solicitud.getMontoSolicitado())
                .plazoMeses(solicitud.getPlazoMeses())
                .estado(solicitud.getEstado())
                .fechaSolicitud(solicitud.getFechaSolicitud())
                .fechaResolucion(solicitud.getFechaResolucion())
                .comentarioResolucion(solicitud.getComentarioResolucion())
                .build();
    }
}