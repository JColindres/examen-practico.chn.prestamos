package com.chn.prestamos.controller;

import com.chn.prestamos.dto.SolicitudPrestamoRequest;
import com.chn.prestamos.dto.SolicitudPrestamoResponse;
import com.chn.prestamos.service.SolicitudPrestamoService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
@Tag(name = "Solicitudes de Préstamo",
        description = "Gestión de solicitudes de préstamos")
public class SolicitudPrestamoController {

    private final SolicitudPrestamoService service;

    @Operation(summary = "Crear nueva solicitud de préstamo")
    @PostMapping
    public SolicitudPrestamoResponse crear(
            @Valid @RequestBody SolicitudPrestamoRequest request) {
        return service.crear(request);
    }

    @Operation(summary = "Listar solicitudes por cliente")
    @GetMapping("/cliente/{idCliente}")
    public List<SolicitudPrestamoResponse> listarPorCliente(
            @PathVariable Integer idCliente) {
        return service.listarPorCliente(idCliente);
    }

    @Operation(summary = "Listar todas las solicitudes pendientes")
    @GetMapping
    public List<SolicitudPrestamoResponse> listarTodos() {
        return service.listarTodos();
    }

    @Operation(summary = "Aprobar solicitud y generar préstamo")
    @PutMapping("/{id}/aprobar")
    public SolicitudPrestamoResponse aprobar(
            @PathVariable Integer id,
            @RequestParam String comentario) {
        return service.aprobar(id, comentario);
    }

    @Operation(summary = "Rechazar solicitud")
    @PutMapping("/{id}/rechazar")
    public SolicitudPrestamoResponse rechazar(
            @PathVariable Integer id,
            @RequestParam String comentario) {
        return service.rechazar(id, comentario);
    }
}