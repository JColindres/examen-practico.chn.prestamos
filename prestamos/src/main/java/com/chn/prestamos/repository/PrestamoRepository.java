package com.chn.prestamos.repository;

import com.chn.prestamos.entity.Pago;
import com.chn.prestamos.entity.Prestamo;
import com.chn.prestamos.entity.SolicitudPrestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {
    boolean existsByIdSolicitud(SolicitudPrestamo solicitud);

    List<Prestamo> findByClienteIdCliente(Integer idCliente);
}
