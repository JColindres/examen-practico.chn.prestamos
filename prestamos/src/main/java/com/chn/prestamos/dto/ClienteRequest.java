package com.chn.prestamos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public class ClienteRequest {

    @Schema(example = "Juan")
    @NotBlank
    private String nombre;

    @Schema(example = "Pérez")
    @NotBlank
    private String apellido;

    @Schema(example = "1234567890101")
    @NotBlank
    private String numeroIdentificacion;

    @Schema(example = "1995-05-10")
    @NotNull
    private LocalDate fechaNacimiento;

    private String direccion;

    @Email
    private String correo;

    private String telefono;
}
