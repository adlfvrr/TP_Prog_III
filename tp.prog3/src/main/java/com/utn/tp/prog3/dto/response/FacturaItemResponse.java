package com.utn.tp.prog3.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FacturaItemResponse {

    private Long id_items;
    private double monto;
    private double cantidad;
    private Long id_factura;
    private String detalle;

}
