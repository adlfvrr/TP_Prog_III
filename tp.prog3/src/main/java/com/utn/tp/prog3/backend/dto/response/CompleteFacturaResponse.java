package com.utn.tp.prog3.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CompleteFacturaResponse {

    private Long id_factura;
    private Date fecha_factura;
    private Long id_tercero;
    private int numero;
    private List<FacturaItemResponse> itemResponseList;

}
