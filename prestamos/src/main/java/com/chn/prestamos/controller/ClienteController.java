package com.chn.prestamos.controller;

import com.chn.prestamos.dto.*;
import com.chn.prestamos.service.ClienteService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Operaciones relacionadas con la gestión de clientes")
public class ClienteController {

    private final ClienteService service;

    @Operation(summary = "Crear nuevo cliente")
    @PostMapping
    public ClienteResponse crear(@Valid @RequestBody ClienteRequest request) {
        return service.crear(request);
    }

    @Operation(summary = "Listar todos los clientes")
    @GetMapping
    public List<ClienteResponse> listar() {
        return service.listar();
    }

    @Operation(summary = "Actualizar cliente existente")
    @PutMapping("/{id}")
    public ClienteResponse actualizar(
            @Parameter(description = "ID del cliente")
            @PathVariable Integer id,
            @Valid @RequestBody ClienteRequest request) {

        return service.actualizar(id, request);
    }

    @Operation(summary = "Eliminar cliente por ID")
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        service.eliminar(id);
    }
}