package com.utn.tp.prog3.ui.client.service;

import com.utn.tp.prog3.backend.dto.request.AddTerceroRequest;
import com.utn.tp.prog3.backend.dto.request.UpdateTerceroRequest;
import com.utn.tp.prog3.backend.dto.response.TerceroResponse;
import com.utn.tp.prog3.ui.client.ApiClient;
import com.utn.tp.prog3.ui.dto.PageResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class TerceroService {

    private final ApiClient apiClient;

    public TerceroService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public PageResponse<TerceroResponse> findAll(String nombre, String cuit, String sitIVA,
                                         String direccion, String localidad, String provincia,
                                         String telefono, String tipoSaldo,
                                         int page, int size) {
        //Construimos la URL base con los parámetros que realmente tienen valor
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/terceros")
                .queryParam("page", page)
                .queryParam("size", size);
        //Acá no hacemos comprobaciones, pues ya están hechas en el back (servicio)
            builder.queryParam("sitIVA", sitIVA);
            builder.queryParam("nombre", nombre);
            builder.queryParam("cuit", cuit);
            builder.queryParam("direccion", direccion);
            builder.queryParam("localidad", localidad);
            builder.queryParam("provincia", provincia);
            builder.queryParam("telefono", telefono);
            builder.queryParam("tipoSaldo", tipoSaldo);

            //Convertimos toda nuestra URL a string
            String url = builder.build().toUriString();

        //Usamos ParameterizedTypeReference para mantener el tipo genérico Page<TerceroResponse>
        ParameterizedTypeReference<PageResponse<TerceroResponse>> typeRef =
                new ParameterizedTypeReference<PageResponse<TerceroResponse>>() {};

        return apiClient.get(url, typeRef);

    }

    public TerceroResponse findById(Long id) {
        return apiClient.getById("/terceros/" + id, TerceroResponse.class);
    }

    public TerceroResponse create(AddTerceroRequest request) {
        return apiClient.post("/terceros", request, TerceroResponse.class);
    }

    public TerceroResponse update(Long id, UpdateTerceroRequest request) {
        return apiClient.put("/terceros/" + id, request, TerceroResponse.class);
    }

    public void delete(Long id) {
        apiClient.delete("/terceros/" + id);
    }

    //Método para la view de Facturas, para poder ver todos los terceros
    public List<TerceroResponse> findAllSimple() {
        //Usamos una página grande.
        PageResponse<TerceroResponse> page = findAll(null, null, null, null, null, null, null, null, 0, 1000);
        return page.getContent();
    }

}
