package com.utn.tp.prog3.backend.model;

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
    private Long id;
    @Column(nullable = false, name = "fecha_factura")
    private Date fechaFactura;
    @ManyToOne
    @JoinColumn(name = "id_tercero")
    private Tercero tercero;
    @Column(name = "numero")
    private Integer numero;


}
