package com.utn.tp.prog3.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class CompletePagoResponse {

    private Long idPago;
    private Long idTercero;
    private Date fechaPago;
    private double montoPago;
    private String modoPago;
    private PagoDetalleResponse detalleResponse;

}
