package com.utn.tp.prog3.ui.client.service;

import com.utn.tp.prog3.backend.dto.request.AddPagoRequest;
import com.utn.tp.prog3.backend.dto.response.CompletePagoResponse;
import com.utn.tp.prog3.ui.client.ApiClient;
import com.utn.tp.prog3.ui.dto.PageResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;

@Service
public class PagoService {

    private final ApiClient apiClient;

    public PagoService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public PageResponse<CompletePagoResponse> findAll(String cuit, String modoPago, Date fechaPago,
                                                      int page, int size) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/pagos")
                .queryParam("page", page)
                .queryParam("size", size);

            builder.queryParam("cuit", cuit);
            builder.queryParam("modoPago", modoPago);

        if (fechaPago != null) {
            builder.queryParam("fechaPago", fechaPago.getTime());
        }

        String url = builder.build().toUriString();

        ParameterizedTypeReference<PageResponse<CompletePagoResponse>> typeRef =
                new ParameterizedTypeReference<PageResponse<CompletePagoResponse>>() {};

        return apiClient.get(url, typeRef);
    }

    public CompletePagoResponse findById(Long id) {
        return apiClient.getById("/pagos/" + id, CompletePagoResponse.class);
    }

    public CompletePagoResponse create(AddPagoRequest request) {
        return apiClient.post("/pagos", request, CompletePagoResponse.class);
    }

    public void delete(Long id) {
        apiClient.delete("/pagos/" + id);
    }
}
