package com.utn.tp.prog3.dto.request;

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
public class UserAuthLoginRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio.")
    @Size(min = 4, max = 70, message = "El nombre de usuario tiene un mínimo de 4 caracteres y máximo de 20.")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    private String password;

}


