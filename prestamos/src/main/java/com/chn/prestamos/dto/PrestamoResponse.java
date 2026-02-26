package com.chn.prestamos.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PrestamoResponse {

    private Integer idPrestamo;
    private BigDecimal montoAprobado;
    private BigDecimal tasaInteres;
    private BigDecimal saldoPendiente;
    private LocalDateTime fechaInicio;
    private Integer totalPagos;
}