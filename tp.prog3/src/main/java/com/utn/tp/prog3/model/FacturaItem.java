package com.utn.tp.prog3.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "facturas_items")
@Getter
@Setter
public class FacturaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_items;
    @Column(nullable = false, name = "monto")
    private double monto;
    @Column(nullable = false, name = "cantidad")
    private double cantidad;
    @ManyToOne
    @JoinColumn(name = "id_factura")
    private Factura factura;
    @Column(name = "detalle",length = 300)
    private String detalle;


}
