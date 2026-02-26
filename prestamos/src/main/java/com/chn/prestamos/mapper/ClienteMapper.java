package com.chn.prestamos.mapper;

import com.chn.prestamos.dto.*;
import com.chn.prestamos.entity.Cliente;

public class ClienteMapper {

    public static Cliente toEntity(ClienteRequest request) {
        return Cliente.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .numeroIdentificacion(request.getNumeroIdentificacion())
                .fechaNacimiento(request.getFechaNacimiento())
                .direccion(request.getDireccion())
                .correo(request.getCorreo())
                .telefono(request.getTelefono())
                .build();
    }

    public static ClienteResponse toResponse(Cliente cliente) {
        return ClienteResponse.builder()
                .idCliente(cliente.getIdCliente())
                .nombre(cliente.getNombre())
                .apellido(cliente.getApellido())
                .numeroIdentificacion(cliente.getNumeroIdentificacion())
                .fechaNacimiento(cliente.getFechaNacimiento())
                .direccion(cliente.getDireccion())
                .correo(cliente.getCorreo())
                .telefono(cliente.getTelefono())
                .fechaCreacion(cliente.getFechaCreacion())
                .build();
    }
}