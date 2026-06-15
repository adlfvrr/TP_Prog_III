package com.utn.tp.prog3.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class UpdateTerceroRequest {

    private String nombre;
    private String cuitl;
    private String sitIVA;
    private String direccion;
    private String localidad;
    private String provincia;
    private String telefono;
    private double saldo_apertura;
    private String tipo_saldo;

}
