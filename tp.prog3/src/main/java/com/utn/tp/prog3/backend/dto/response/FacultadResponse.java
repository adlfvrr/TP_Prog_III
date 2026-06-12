package com.utn.tp.prog3.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacultadResponse {

    private Long id;
    private String nombre;
    private String direccion;
    private String cuit;
    private int sucursal;
    private String telefono;
    private String email;
    private String defectos;

    public FacultadResponse(Long id,String nombre, String direccion, String cuit, int sucursal, String telefono, String email, boolean defecto){
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.cuit = cuit;
        this.sucursal = sucursal;
        this.telefono = telefono;
        this.email = email;
        this.defectos = (!defecto) ? "Sin defectos" : "Con defectos";
    }

}

/*
    DATOS A MOSTRAR A LA HORA DE CONSULTAR POR LA FACULTAD
    ID
    NOMBRE
	DIRECCION
	CUIT
	SUCURSAL
	TELEFONOS
	EMAIL
	DEFECTO
 */