package com.chn.prestamos.repository;

import com.chn.prestamos.entity.EstadoSolicitud;
import com.chn.prestamos.entity.SolicitudPrestamo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudPrestamoRepository
        extends JpaRepository<SolicitudPrestamo, Integer> {

    List<SolicitudPrestamo> findByClienteIdCliente(Integer idCliente);

    List<SolicitudPrestamo> findByEstado(EstadoSolicitud estado);
}
