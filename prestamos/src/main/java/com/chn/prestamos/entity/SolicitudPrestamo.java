package com.chn.prestamos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Solicitud_Prestamo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudPrestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Integer idSolicitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal montoSolicitado;

    @Column(nullable = false)
    private Integer plazoMeses;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estado;

    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaResolucion;
    private String comentarioResolucion;

    @PrePersist
    public void prePersist() {
        this.fechaSolicitud = LocalDateTime.now();
        this.estado = EstadoSolicitud.PENDIENTE;
    }
}
