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
    @Column(nullable = false, name = "monto", precision = 8, scale = 2)
    private double monto;
    @Column(nullable = false, name = "cantidad", precision = 9, scale = 3)
    private double cantidad;
    @Column(nullable = false, name = "id_factura")
    @ManyToOne
    @JoinColumn(name = "id_factura")
    private Long id_factura;
    @Column(name = "detalle",length = 300)
    private String detalle;


}
