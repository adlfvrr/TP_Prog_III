package com.utn.tp.prog3.backend.service.implementation;

import com.utn.tp.prog3.backend.dto.request.UserAuthLoginRequest;
import com.utn.tp.prog3.backend.dto.request.UserAuthRegisterRequest;
import com.utn.tp.prog3.backend.dto.response.UserAuthResponse;
import com.utn.tp.prog3.backend.exception.EntityAlreadyExistsException;
import com.utn.tp.prog3.backend.model.User;
import com.utn.tp.prog3.backend.repository.UserRepository;
import com.utn.tp.prog3.backend.service.Iservices.IUserService;
import com.utn.tp.prog3.backend.service.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public UserAuthResponse register(UserAuthRegisterRequest request) {
        if(request.getEmail().isBlank()){
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        if(request.getUsername().isBlank()){
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
        }
        if(request.getPassword().isBlank()){
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        if(this.userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new EntityAlreadyExistsException("El email ya está registrado");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(this.passwordEncoder.encode(request.getPassword()));

        User savedUser = this.userRepository.save(user);
        String token = this.jwtTokenProvider.generateToken(request.getUsername(), savedUser.getRole().name());

        return new UserAuthResponse(token, savedUser.getUsername(), savedUser.getRole().name());
    }

    @Override
    public UserAuthResponse login(UserAuthLoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new IllegalArgumentException("Datos inválidos."));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("Datos inválidos");
        }

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole().name());
        return new UserAuthResponse(token, user.getUsername(), user.getRole().name());
    }
}
