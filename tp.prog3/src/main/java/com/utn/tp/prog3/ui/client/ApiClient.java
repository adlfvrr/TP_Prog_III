package com.utn.tp.prog3.ui.client;

import com.vaadin.flow.server.VaadinSession;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ApiClient {

    //Indicamos nuestra URL, que será la misma que los controladores de nuestro backend, para que el cliente pueda consumir los servicios
    private static final String API_BASE_URL = "http://localhost:8080/tp";

    //Creamos un RestTemplate para realizar las solicitudes HTTP al backend
    private final RestTemplate restTemplate;

    //Método auxiliar para obtener el token de la sesión
    private String getTokenFromSession() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session == null) return null;

        Object token = session.getAttribute("jwtToken");
        //Comprobamos si existe y si es string -> si lo es, lo asignamos
        return (token instanceof String) ? (String) token : null;
    }

    public ApiClient() {
        this.restTemplate = new RestTemplate();

        //Añadimos un interceptor = filtro que se ejecutará antes de cada petición HTTP. Nos servirá para los tokens JWT
        this.restTemplate.getInterceptors().add((request, body, execution) -> {

            //Obtenemos el token de la sesión
            String token = this.getTokenFromSession();

            //Evitar enviar Authorization en endpoints de autenticación pública (login/register). Si la petición apunta a /auth/login o /auth/register,
            //no añadimos el header
            String path = request.getURI() != null ? request.getURI().getPath() : "";
            boolean isAuthEndpoint = path != null && (path.endsWith("/auth/login") || path.endsWith("/auth/register"));

            //Si no es un endpoint de autenticación (login - register)
            if (!isAuthEndpoint) {
                //Si existe un token no nulo y no vacío, lo añadimos al header "Authorization" con el esquema "Bearer"
                if (token != null && !token.isEmpty()) {
                    request.getHeaders().setBearerAuth(token);
                }
            }

            //Retornamos la ejecución de la petición
            return execution.execute(request, body);
        });
        //Entonces, a la hora de realizar peticiones HTTP, le añadimos un filtro que verifica si hay un token JWT en la sesión de Vaadin.
        //Si no hay, lo añade automáticamente a la sesión de Vaadin.
    }

    //Guardamos el token de la sesión. Se llamará después del login.
    public void saveToken(String token) {
        VaadinSession session = VaadinSession.getCurrent();

        if (session != null) {
            session.setAttribute("jwtToken", token);
        }
    }

    //Borramos el token de la sesión. Se llamará en caso de posible logout
    public void clearToken() {
        VaadinSession session = VaadinSession.getCurrent();

        if (session != null) {
            session.setAttribute("jwtToken", null);
        }
    }

    public String getToken() {
        return this.getTokenFromSession();
    }

    /*
    Ahora realizamos métodos HTTP genéricos, que serán ejecutados según la petición y la entidad, como también la response
    ¿Por qué genéricos? Porque al ser un cliente común, no sabemos qué tipo de entidad ni response vamos a manejar, por lo
     que utilizamos genéricos para adaptarnos a cualquier tipo de respuesta o entidad que necesitemos manejar en el cliente.
     */

    /*
    GET
    Por ahora vacío, puesto que vamos a manejar la mayoría de las peticiones con POST, pero lo dejamos preparado para futuras implementaciones
    path: Ruta de la API sumada a nuestra constante
    requestBody: Objeto Java que se envía como Request
    responseType: Clase de respuesta (Response)
     */

    public <T> T get(String path, Class<T> responseType) {
        HttpEntity<Void> entity = new HttpEntity<>(new HttpHeaders());

        ResponseEntity<T> response = this.restTemplate.exchange(
                API_BASE_URL + path,
                HttpMethod.GET,
                entity,
                responseType
        );

        return response.getBody();
    }

    public <T> T post(String path, Object requestBody, Class<T> responseType) {
        //Crear los headers de la petición
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // Content-Type: application/json

        //Construir la entidad HTTP (cabeceras + cuerpo)
        HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);

        //Realizar la petición POST usando exchange (el método más flexible)
        ResponseEntity<T> response = restTemplate.exchange(
                API_BASE_URL + path,   // URL completa
                HttpMethod.POST,       // Verbo HTTP
                entity,                // cuerpo y cabeceras
                responseType          // tipo de dato esperado en la respuesta
        );

        //Devolver solo el cuerpo de la respuesta (el JSON convertido a objeto Java)
        return response.getBody();
    }


    public <T> T put(String path, Object requestBody, Class<T> responseType) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<T> response = restTemplate.exchange(
                API_BASE_URL + path,
                HttpMethod.PUT,
                entity,
                responseType
        );

        return response.getBody();
    }

    public <T> T delete(String path, Class<T> responseType) { //No recibe requestBody, puesto que el delete se realiza mediante un recurso recibido en la URL
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers); //Pasa a ser Void, ya que un delete no devuelve nada

        ResponseEntity<T> response = restTemplate.exchange(
                API_BASE_URL + path,
                HttpMethod.DELETE,
                entity,
                responseType
        );

        return response.getBody();
    }
}
