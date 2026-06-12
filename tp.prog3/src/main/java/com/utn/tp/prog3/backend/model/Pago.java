package com.utn.tp.prog3.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "pagos")
@Getter
@Setter
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_tercero")
    private Tercero tercero;
    @Column(name = "fecha_pago", nullable = false)
    private Date fecha_pago;
    @Column(name = "monto_pago")
    private double monto_pago;
    @Column(name = "modo_pago", nullable = false, length = 20)
    private String modo_pago;

}
