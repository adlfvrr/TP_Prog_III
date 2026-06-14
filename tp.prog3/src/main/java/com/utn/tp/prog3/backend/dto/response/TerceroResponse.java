package com.utn.tp.prog3.backend.dto.response;

import com.utn.tp.prog3.backend.model.SitIVA;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class TerceroResponse {

    private Long idTercero;
    private String nombre;
    private String cuitl;
    private SitIVA sitIVA;
    private String direccion;
    private String localidad;
    private String provincia;
    private String telefono;
    private double saldo_apertura;
    private String tipo_saldo;


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
