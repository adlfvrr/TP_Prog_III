package com.utn.tp.prog3.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class UpdateFacultadRequest {

    private String nombre;
    private String direccion;
    private String cuit;
    private Integer sucursal;
    private String telefono;
    private String email;
    private boolean defecto = false;

}
