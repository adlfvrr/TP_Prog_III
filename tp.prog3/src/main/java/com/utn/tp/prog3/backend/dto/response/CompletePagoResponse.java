package com.utn.tp.prog3.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class CompletePagoResponse {

    private Long id_pagos;
    private Long id_tercero;
    private Date fecha_pago;
    private double monto_pago;
    private String modo_pago;
    private PagoDetalleResponse detalleResponse;

}
