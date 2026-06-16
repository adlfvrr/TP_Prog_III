package com.utn.tp.prog3.ui.client.service;

import com.utn.tp.prog3.backend.dto.request.AddFacturaRequest;
import com.utn.tp.prog3.backend.dto.response.CompleteFacturaResponse;
import com.utn.tp.prog3.backend.dto.response.TerceroResponse;
import com.utn.tp.prog3.ui.client.ApiClient;
import com.utn.tp.prog3.ui.dto.PageResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.List;

@Service
public class FacturaService {

    private final ApiClient apiClient;

    public FacturaService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }


    public PageResponse<CompleteFacturaResponse> findAll(Integer numero, String cuit, Date fechaFactura,
                                                         int page, int size) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/facturas")
                .queryParam("page", page)
                .queryParam("size", size);

        builder.queryParam("numero", numero);
        builder.queryParam("cuit", cuit);
        if (fechaFactura != null) {
            builder.queryParam("fechaFactura", fechaFactura.getTime());
        }

        String url = builder.build().toUriString();

        ParameterizedTypeReference<PageResponse<CompleteFacturaResponse>> typeRef =
                new ParameterizedTypeReference<PageResponse<CompleteFacturaResponse>>() {
                };

        return apiClient.get(url, typeRef);
    }

    public CompleteFacturaResponse findById(Long id) {
        return apiClient.getById("/facturas/" + id, CompleteFacturaResponse.class);
    }

    public CompleteFacturaResponse create(AddFacturaRequest request) {
        return apiClient.post("/facturas", request, CompleteFacturaResponse.class);
    }

    public void delete(Long id) {
        apiClient.delete("/facturas/" + id);
    }

    // Obtener lista de terceros (para el ComboBox)
    public List<TerceroResponse> getTerceros() {
        // Usamos el endpoint de terceros con tamaño grande para obtener todos
        // Asumimos que el total de terceros es manejable (ej. < 1000)
        String url = UriComponentsBuilder.fromPath("/tp/terceros")
                .queryParam("page", 0)
                .queryParam("size", 1000)
                .build()
                .toUriString();

        // Necesitamos Page<TerceroResponse>
        ParameterizedTypeReference<PageResponse<TerceroResponse>> typeRef =
                new ParameterizedTypeReference<PageResponse<TerceroResponse>>() {
                };

        PageResponse<TerceroResponse> page = apiClient.get(url, typeRef);
        return page.getContent(); // devuelve la lista
    }

}

