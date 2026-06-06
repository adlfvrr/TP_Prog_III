package com.utn.tp.prog3.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "facturas")
@Getter
@Setter
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_factura;
    @Column(nullable = false, name = "fecha_factura")
    private Date fecha_factura;
    @Column(nullable = false, name = "id_tercero")
    @ManyToOne
    @JoinColumn(name = "id_tercero")
    private Long id_tecero;
    @Column(name = "numero")
    private int numero;


}
