package com.utn.tp.prog3.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "facultad")
@Getter
@Setter
public class Facultad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_facultad;
    @Column(nullable = false, name = "nombre", length = 70)
    private String nombre;
    @Column(nullable = false, name = "direccion", length = 100)
    private String direccion;
    @Column(nullable = false, length = 15)
    private String cuit;
    @Column(nullable = false, name = "sucursal")
    private int sucursal;
    @Column(name = "telefonos", length = 120)
    private String telefono;
    @Column(name = "email", length = 70)
    @Email
    private String email;
    @Column(name = "defecto")
    private boolean defecto = false;

}
