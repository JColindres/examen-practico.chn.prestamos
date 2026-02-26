package com.chn.prestamos.dto;

import com.chn.prestamos.entity.EstadoSolicitud;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class SolicitudPrestamoResponse {

    private Integer idSolicitud;
    private Integer idCliente;
    private BigDecimal montoSolicitado;
    private Integer plazoMeses;
    private EstadoSolicitud estado;
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaResolucion;
    private String comentarioResolucion;
}