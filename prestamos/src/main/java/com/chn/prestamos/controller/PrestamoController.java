package com.chn.prestamos.controller;

import com.chn.prestamos.dto.PagoRequest;
import com.chn.prestamos.dto.PagoResponse;
import com.chn.prestamos.dto.PrestamoResponse;
import com.chn.prestamos.service.PrestamoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
@RequiredArgsConstructor
public class PrestamoController {

    private final PrestamoService prestamoService;

    @Operation(summary = "Listar Prestamos por Cliente")
    @GetMapping("/cliente/{idCliente}")
    public List<PrestamoResponse> listarPorCliente(@PathVariable Integer idCliente) {
        return prestamoService.listarPorCliente(idCliente);
    }

    @Operation(summary = "Realizar Pagos")
    @PostMapping("/{idPrestamo}/pagos")
    public ResponseEntity<String> registrarPago(
            @PathVariable Integer idPrestamo,
            @RequestBody PagoRequest request) {

        prestamoService.registrarPago(idPrestamo, request);

        return ResponseEntity.ok("Pago registrado correctamente");
    }

    @Operation(summary = "Obtener Pagos")
    @GetMapping("/{idPrestamo}/listapagos")
    public List<PagoResponse> listarPago(@PathVariable Integer idPrestamo) {
        return prestamoService.listarPagosPorPrestamo(idPrestamo);
    }
}
