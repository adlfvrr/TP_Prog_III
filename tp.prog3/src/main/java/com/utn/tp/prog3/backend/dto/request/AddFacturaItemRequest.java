package com.utn.tp.prog3.backend.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class AddFacturaItemRequest {

    @NotEmpty(message = "Campo monto obligatorio")
    private double monto;
    @NotEmpty(message = "Campo cantidad obligatorio")
    private double cantidad;
    @NotEmpty(message = "Campo idFactura obligatorio")
    private Long id_factura;
    @Size(max = 300)
    private String detalle;

}
