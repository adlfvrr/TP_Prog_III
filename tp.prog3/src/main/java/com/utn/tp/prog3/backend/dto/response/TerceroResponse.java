package com.utn.tp.prog3.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TerceroResponse {

    private Long idTercero;
    private String nombre;
    private String cuitl;
    private String sitIVA;
    private String direccion;
    private String localidad;
    private String provincia;
    private String telefono;
    private double saldo_apertura;
    private String tipo_saldo;

    public TerceroResponse(Long id, String nombre, String cuit, String sitIVA, String direccion, String localidad, String provincia, String telefono, double saldo_apertura, String tipo_saldo){
        this.idTercero = id;
        this.nombre = nombre;
        this.cuitl = cuit;
        this.sitIVA = sitIVA;
        this.direccion = direccion;
        this.localidad = localidad;
        this.provincia = provincia;
        this.telefono = telefono;
        this.saldo_apertura = saldo_apertura;
        this.tipo_saldo = tipo_saldo;
    }
}
/*
    DATOS A MOSTRAR EN EL RESPONSE
    ID
    NOMBRE
    CUITL
    SITUACION IVA
    DIRECCIÓN
    LOCALIDAD
    PROVINCIA
    TELÉFONO
    SALDO DE APERTURA
    TIPO DE SALDO
 */
