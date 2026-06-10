package com.utn.tp.prog3.service.Iservices;

import com.utn.tp.prog3.dto.request.UserAuthLoginRequest;
import com.utn.tp.prog3.dto.request.UserAuthRegisterRequest;
import com.utn.tp.prog3.dto.response.UserAuthResponse;

public interface IUserService {

        UserAuthResponse register(UserAuthRegisterRequest request);
        UserAuthResponse login(UserAuthLoginRequest request);

}
