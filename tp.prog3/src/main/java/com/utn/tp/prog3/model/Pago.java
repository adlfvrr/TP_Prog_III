package com.utn.tp.prog3.model;

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
    private Long id_pagos;
    @Column(name = "id_tercero", nullable = false)
    @ManyToOne
    @JoinColumn(name = "id_tercero")
    private Long id_tercero;
    @Column(name = "fecha_pago", nullable = false)
    private Date fecha_pago;
    @Column(name = "monto_pago", precision = 12, scale = 2)
    private double monto_pago;
    @Column(name = "modo_pago", nullable = false, length = 20)
    private String modo_pago;

}
