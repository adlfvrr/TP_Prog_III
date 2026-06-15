package com.utn.tp.prog3.backend.service.Iservices;

import com.utn.tp.prog3.backend.dto.request.AddTerceroRequest;
import com.utn.tp.prog3.backend.dto.request.UpdateTerceroRequest;
import com.utn.tp.prog3.backend.dto.response.TerceroResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ITerceroService {

    //Añadimos argumentos para el filtrado
    Page<TerceroResponse> findAll(String nombre, String cuitl, String sitIVA, String direccion, String localidad, String provincia, String telefono, String tipo_saldo, Pageable page);
    TerceroResponse findById(Long id);
    TerceroResponse addTercero(AddTerceroRequest request);
    TerceroResponse updateTercero(UpdateTerceroRequest request, Long id);
    void deleteTercero(Long id);

}
