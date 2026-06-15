package com.utn.tp.prog3.ui.client.service;

import com.utn.tp.prog3.backend.dto.request.AddFacultadRequest;
import com.utn.tp.prog3.backend.dto.request.UpdateFacultadRequest;
import com.utn.tp.prog3.backend.dto.response.FacultadResponse;
import com.utn.tp.prog3.ui.client.ApiClient;
import com.utn.tp.prog3.ui.dto.PageResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class FacultadService {

    private final ApiClient apiClient;

    public FacultadService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public PageResponse<FacultadResponse> findAll(String nombre, String direccion, String cuit,
                                                  String telefono, String email, int page, int size) {
        //Construimos la URL base con los parámetros que realmente tienen valor
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/facultades")
                .queryParam("page", page)
                .queryParam("size", size);
        //Acá no hacemos comprobaciones, pues ya están hechas en el back (servicio)
        builder.queryParam("nombre", nombre);
        builder.queryParam("direccion", direccion);
        builder.queryParam("cuit", cuit);
        builder.queryParam("telefono", telefono);
        builder.queryParam("email", email);

        //Convertimos toda nuestra URL a string
        String url = builder.build().toUriString();

        ParameterizedTypeReference<PageResponse<FacultadResponse>> typeRef =
                new ParameterizedTypeReference<PageResponse<FacultadResponse>>() {
                };

        return apiClient.get(url, typeRef);

    }

    public FacultadResponse findById(Long id) {
        return apiClient.getById("/facultades/" + id, FacultadResponse.class);
    }

    public FacultadResponse create(AddFacultadRequest request) {
        return apiClient.post("/facultades", request, FacultadResponse.class);
    }

    public FacultadResponse update(Long id, UpdateFacultadRequest request) {
        return apiClient.put("/facultades/" + id, request, FacultadResponse.class);
    }

    public void delete(Long id) {
        apiClient.delete("/facultades/" + id);
    }
}


