package com.chn.prestamos.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ClienteResponse {

    private Integer idCliente;
    private String nombre;
    private String apellido;
    private String numeroIdentificacion;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String correo;
    private String telefono;
    private LocalDateTime fechaCreacion;
}
