package com.utn.tp.prog3.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class AddFacultadRequest {

    @NotBlank(message = "El campo de nombre es obligatorio")
    @Size(max = 70)
    private String nombre;
    @NotBlank(message = "El campo de dirección es obligatorio")
    @Size(max = 100)
    private String direccion;
    @NotBlank(message = "El campo de CUIT es obligatorio")
    @Size(max = 15)
    private String cuit;
    @NotEmpty(message = "El campo de sucursal es obligatorio")
    private Integer sucursal;
    @Size(max = 120)
    private String telefono;
    @Size(max = 70)
    @Email
    private String email;
    private boolean defecto = false;



}
