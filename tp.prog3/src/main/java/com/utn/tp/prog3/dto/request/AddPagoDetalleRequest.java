package com.utn.tp.prog3.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class AddPagoDetalleRequest {

    @NotBlank(message = "Campo instrument number obligatorio")
    @Size(max = 15)
    private String instrumentNumber;
    @NotEmpty(message = "Campo instrument date obligatorio")
    private Date instrumentDate;
    @Size(max = 100)
    private String banco;
    private boolean pagoRealizado = false;
    @NotEmpty(message = "Campo idPago obligatorio")
    private Long id_pago;

}
