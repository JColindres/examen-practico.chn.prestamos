package com.chn.prestamos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Prestamo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prestamo")
    private Integer idPrestamo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", nullable = false)
    private SolicitudPrestamo idSolicitud;

    @Column(name = "monto_aprobado",nullable = false)
    private BigDecimal montoAprobado;

    @Column(name = "tasa_interes", nullable = false)
    private BigDecimal tasaInteres;

    @Column(name = "saldo_pendiente", nullable = false)
    private BigDecimal saldoPendiente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPrestamo estadoPago; // AL_DIA, ATRASADO

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @OneToMany(mappedBy = "prestamo", cascade = CascadeType.ALL)
    private List<Pago> pagos;

    @PrePersist
    public void prePersist() {
        this.fechaInicio = LocalDateTime.now();
        this.estadoPago = EstadoPrestamo.AL_DIA;
    }
}
