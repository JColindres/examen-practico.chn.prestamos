package com.chn.prestamos.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PagoResponse {

    private Integer idPago;
    private BigDecimal montoPago;
    private LocalDateTime fechaPago;
}
