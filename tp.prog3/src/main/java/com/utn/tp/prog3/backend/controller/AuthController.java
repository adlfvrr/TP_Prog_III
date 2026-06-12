package com.utn.tp.prog3.backend.controller;

import com.utn.tp.prog3.backend.dto.request.UserAuthLoginRequest;
import com.utn.tp.prog3.backend.dto.request.UserAuthRegisterRequest;
import com.utn.tp.prog3.backend.dto.response.UserAuthResponse;
import com.utn.tp.prog3.backend.service.Iservices.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tp/auth")
@AllArgsConstructor
public class AuthController {

    //Controlador de autenticación

    private final IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserAuthResponse> register(@RequestBody UserAuthRegisterRequest request){
        return ResponseEntity.ok(this.userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthResponse> login(@RequestBody UserAuthLoginRequest request){
        return ResponseEntity.ok(this.userService.login(request));
    }

}
