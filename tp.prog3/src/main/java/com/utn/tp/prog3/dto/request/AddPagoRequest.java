package com.utn.tp.prog3.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class AddPagoRequest {

    @NotEmpty(message = "Campo idTercero obligatorio")
    private Long id_tercero;
    @NotEmpty(message = "Campo fecha de pago obligatorio")
    private Date fecha_pago;
    private double monto_pago;
    @NotBlank(message = "Campo modo de pago obligatorio")
    private String modo_pago;
    @NotEmpty(message = "El detalle del pago es obligatorio")
    private AddPagoDetalleRequest detalleRequest;


}
