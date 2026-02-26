package com.chn.prestamos.mapper;

import com.chn.prestamos.dto.AuthResponse;
import com.chn.prestamos.dto.ClienteRequest;
import com.chn.prestamos.dto.RegistroRequest;
import com.chn.prestamos.entity.Cliente;
import com.chn.prestamos.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    public AuthResponse toAuthResponse(Usuario usuario, String jwt) {
        return new AuthResponse(jwt, usuario.getUsername(), usuario.getRol());
    }

    public static Usuario toEntity(RegistroRequest request) {
        return Usuario.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .rol(request.getRol())
                .build();
    }
}
