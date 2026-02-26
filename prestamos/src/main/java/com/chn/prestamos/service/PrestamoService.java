package com.chn.prestamos.service;

import com.chn.prestamos.dto.PagoRequest;
import com.chn.prestamos.dto.PagoResponse;
import com.chn.prestamos.dto.PrestamoResponse;
import com.chn.prestamos.entity.Pago;
import com.chn.prestamos.entity.Prestamo;
import com.chn.prestamos.repository.PagoRepository;
import com.chn.prestamos.repository.PrestamoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final PagoRepository pagoRepository;

    public List<PrestamoResponse> listarPorCliente(Integer idCliente) {

        return prestamoRepository.findByClienteIdCliente(idCliente)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void registrarPago(Integer idPrestamo, PagoRequest request) {

        Prestamo prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        if (request.getMontoPago().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Monto inválido");
        }

        if (prestamo.getSaldoPendiente().compareTo(request.getMontoPago()) < 0) {
            throw new RuntimeException("El pago excede el saldo pendiente");
        }

        Pago pago = Pago.builder()
                .prestamo(prestamo)
                .montoPago(request.getMontoPago())
                .build();

        pagoRepository.save(pago);

        BigDecimal nuevoSaldo = prestamo.getSaldoPendiente()
                .subtract(request.getMontoPago());

        prestamo.setSaldoPendiente(nuevoSaldo);

        prestamoRepository.save(prestamo);
    }

    private PrestamoResponse toResponse(Prestamo prestamo) {

        PrestamoResponse response = new PrestamoResponse();
        response.setIdPrestamo(prestamo.getIdPrestamo());
        response.setMontoAprobado(prestamo.getMontoAprobado());
        response.setTasaInteres(prestamo.getTasaInteres());
        response.setSaldoPendiente(prestamo.getSaldoPendiente());
        response.setFechaInicio(prestamo.getFechaInicio());
        response.setTotalPagos(
                prestamo.getPagos() != null ? prestamo.getPagos().size() : 0
        );

        return response;
    }

    public List<PagoResponse> listarPagosPorPrestamo(Integer idPrestamo) {

        List<Pago> pagos = pagoRepository.findByPrestamoIdPrestamo(idPrestamo);

        return pagos.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private PagoResponse toResponse(Pago pago) {

        PagoResponse response = new PagoResponse();
        response.setIdPago(pago.getIdPago());
        response.setMontoPago(pago.getMontoPago());
        response.setFechaPago(pago.getFechaPago());

        return response;
    }
}