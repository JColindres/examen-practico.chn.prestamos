package com.chn.prestamos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
public class SolicitudPrestamoRequest {

    @Schema(example = "1")
    @NotNull
    private Integer idCliente;

    @Schema(example = "10000.00")
    @NotNull
    @Positive
    private BigDecimal montoSolicitado;

    @Schema(example = "24")
    @NotNull
    @Min(6)
    private Integer plazoMeses;
}
