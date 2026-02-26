package com.chn.prestamos.service;

import com.chn.prestamos.dto.RegistroRequest;
import com.chn.prestamos.entity.Usuario;
import com.chn.prestamos.mapper.UsuarioMapper;
import com.chn.prestamos.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;

    public String generarToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Usuario validarUsuario(String username, String password) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (passwordEncoder.matches(password, usuario.getPassword())) {
            return usuario;
        } else {
            throw new RuntimeException("Contraseña incorrecta");
        }
    }

    public Usuario registrar(RegistroRequest request) {
        if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        Usuario nuevoUsuario = UsuarioMapper.toEntity(request);

        return usuarioRepository.save(nuevoUsuario);
    }
}
