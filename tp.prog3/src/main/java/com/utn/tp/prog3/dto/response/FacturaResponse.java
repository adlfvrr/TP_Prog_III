package com.utn.tp.prog3.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class FacturaResponse {

    private Long id_factura;
    private Date fecha_factura;
    private Long id_tecero;
    private int numero;

}
