package com.chn.prestamos.controller;

import com.chn.prestamos.dto.LoginRequest;
import com.chn.prestamos.dto.RegistroRequest;
import com.chn.prestamos.entity.Usuario;
import com.chn.prestamos.mapper.UsuarioMapper;
import com.chn.prestamos.service.AuthService;
import io.swagger.v3.oas.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Operation(summary = "Iniciar Sesión")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Usuario usuario = authService.validarUsuario(request.getUsername(), request.getPassword());

            String token = authService.generarToken(usuario.getUsername());

            return ResponseEntity.ok(usuarioMapper.toAuthResponse(usuario, token));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Registrar usuario")
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody RegistroRequest request) {
        try {
            authService.registrar(request);
            return ResponseEntity.ok("Usuario registrado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
