package com.utn.tp.prog3.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserAuthResponse {

    private String token;
    private String username;
    private String role;
    private final String type = "Bearer";
}
