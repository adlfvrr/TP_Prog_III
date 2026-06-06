package com.utn.tp.prog3.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "terceros")
@Getter
@Setter
public class Tercero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_tercero;
    @Column(name = "nombre", nullable = false, length = 70)
    private String nombre;
    @Column(name = "cuitl", nullable = false, length = 15)
    private String cuitl;
    @Column(name = "sitiva", nullable = false, length = 50)
    private String sitIVA;
    @Column(name = "direccion", nullable = false, length = 70)
    private String direccion;
    @Column(name = "localidad", length = 70)
    private String localidad;
    @Column(name = "provincia", length = 20)
    private String provincia;
    @Column(name = "telefonos", length = 120)
    private String telefono;
    @Column(name = "saldo_apertura", precision = 10, scale = 2)
    private double saldo_apertura;
    @Column(name = "tipo_saldo", length = 8)
    private String tipo_saldo;

}
