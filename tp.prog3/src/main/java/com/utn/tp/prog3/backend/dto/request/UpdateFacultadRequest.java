package com.utn.tp.prog3.backend.dto.request;

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
    private Boolean defecto = false;

}
