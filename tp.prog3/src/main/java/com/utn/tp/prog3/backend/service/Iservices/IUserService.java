package com.utn.tp.prog3.backend.service.Iservices;

import com.utn.tp.prog3.backend.dto.request.UserAuthLoginRequest;
import com.utn.tp.prog3.backend.dto.request.UserAuthRegisterRequest;
import com.utn.tp.prog3.backend.dto.response.UserAuthResponse;

public interface IUserService {

        UserAuthResponse register(UserAuthRegisterRequest request);
        UserAuthResponse login(UserAuthLoginRequest request);

}
