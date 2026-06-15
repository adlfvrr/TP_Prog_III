package com.utn.tp.prog3.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class AddTerceroRequest {

    @NotBlank(message = "El campo de nombre es obligatorio")
    @Size(max = 70)
    private String nombre;
    @NotBlank(message = "El campo de CUIT es obligatorio")
    @Size(max = 15)
    private String cuitl;
    @NotBlank(message = "El campo de situación IVA es obligatorio")
    @Size(max = 50)
    private String sitIVA;
    @NotBlank(message = "El campo de dirección es obligatorio")
    @Size(max = 70)
    private String direccion;
    @Size(max = 70)
    private String localidad;
    @Size(max = 20)
    private String provincia;
    @Size(max = 120)
    private String telefono;
    private double saldo_apertura;
    @Size(max = 8)
    private String tipo_saldo;

}
