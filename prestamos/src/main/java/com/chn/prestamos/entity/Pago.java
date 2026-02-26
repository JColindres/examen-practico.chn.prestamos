package com.chn.prestamos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Pago")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer idPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prestamo", nullable = false)
    private Prestamo prestamo;

    @Column(name = "monto_pago", nullable = false)
    private BigDecimal montoPago;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;

    @PrePersist
    public void prePersist() {
        this.fechaPago = LocalDateTime.now();
    }
}
