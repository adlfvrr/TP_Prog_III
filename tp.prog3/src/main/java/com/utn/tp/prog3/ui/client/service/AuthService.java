package com.utn.tp.prog3.ui.client.service;

import com.utn.tp.prog3.backend.dto.request.UserAuthLoginRequest;
import com.utn.tp.prog3.backend.dto.request.UserAuthRegisterRequest;
import com.utn.tp.prog3.backend.dto.response.UserAuthResponse;
import com.utn.tp.prog3.ui.client.ApiClient;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final ApiClient apiClient;

    public AuthService(ApiClient apiClient){
        this.apiClient = apiClient;
    }

    //Register, guardamos usuario nuevo
    public void register(String username, String email, String password){
        if(username == null || username.isEmpty()){
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
        }
        if(email == null || email.isEmpty()){
            throw new IllegalArgumentException("El correo electrónico no puede estar vacío");
        }
        if(password == null || password.isEmpty()){
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }

        UserAuthRegisterRequest request = new UserAuthRegisterRequest(username, email, password);
        //Creamos nuestro request con las credenciales y luego obtenemos el response con el .post de ApiClient(Recibe ruta, request y que devuelve)
        //Más adelante vemos si habrá algún mensaje de éxito o error, por ahora solo lo hacemos para probar la conexión con el backend
        this.apiClient.post("/auth/register", request, Void.class);
    }

    //Login donde se guarda el token de la sesión a través de apiClient
    public void login(String username, String password){
        if(username == null || username.isEmpty())
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
        if(password == null || password.isEmpty())
            throw new IllegalArgumentException("La contraseña no puede estar vacía");

        UserAuthLoginRequest request = new UserAuthLoginRequest(username, password);
        //Creamos nuestro request con las credenciales y luego obtenemos el response con el .post de ApiClient(Recibe ruta, request y que devuelve)
        UserAuthResponse response = this.apiClient.post("/auth/login", request, UserAuthResponse.class);

        //Obtenemos el token del response
        String token = response.getToken();

        //Guardamos el token en nuestro apiClient
        this.apiClient.saveToken(token);

        //Guardamos el username
        apiClient.saveUsername(username);
    }

    //Logout, donde eliminamos el token
    public void logout(){
        this.apiClient.clearToken();
        this.apiClient.saveUsername(null);
    }

    //Verificamos si un usuario está autenticado, si el token existe retorna true
    public boolean isAuthenticated(){
        return this.apiClient.getToken() != null && !this.apiClient.getToken().isEmpty();
    }

    public String getUsername(){
        return this.apiClient.getUsername();
    }

}
