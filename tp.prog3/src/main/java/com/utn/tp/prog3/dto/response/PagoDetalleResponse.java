package com.utn.tp.prog3.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class PagoDetalleResponse {

    private Long id_pagosdetalle;
    private String instrumentNumber;
    private Date instrumentDate;
    private String banco;
    private boolean pagoRealizado = false;
    private Long id_pago;

}
