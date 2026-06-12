package com.utn.tp.prog3.backend.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class AddFacturaRequest {

    @NotEmpty(message = "Campo fecha de factura obligatorio")
    private Date fecha_factura;
    @NotEmpty(message = "Campo idTercero obligatorio")
    private Long id_tecero;
    private int numero;
    @NotEmpty(message = "Al menos un item obligatorio")
    private List<AddFacturaItemRequest> itemRequestList;
}
