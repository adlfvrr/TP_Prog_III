package com.utn.tp.prog3.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "pagos_detalle")
@Getter
@Setter
public class PagoDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pagosdetalle;
    @Column(name = "instrumentnumber", nullable = false, length = 15)
    private String instrumentNumber;
    @Column(name = "instrumentdate", nullable = false)
    private Date instrumentDate;
    @Column(name = "banco", length = 100)
    private String banco;
    @Column(name = "pagorealizado")
    private boolean pagoRealizado = false;
    @Column(name = "id_pagos", nullable = false)
    @ManyToOne
    @JoinColumn(name = "id_pago")
    private Long id_pago;

}
