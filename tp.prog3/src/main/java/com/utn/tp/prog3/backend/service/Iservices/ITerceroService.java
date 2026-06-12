package com.utn.tp.prog3.backend.service.Iservices;

import com.utn.tp.prog3.backend.dto.request.AddTerceroRequest;
import com.utn.tp.prog3.backend.dto.request.UpdateTerceroRequest;
import com.utn.tp.prog3.backend.dto.response.TerceroResponse;

import java.util.List;

public interface ITerceroService {

    List<TerceroResponse> findAll();
    TerceroResponse findById(Long id);
    TerceroResponse addTercero(AddTerceroRequest request);
    TerceroResponse updateTercero(UpdateTerceroRequest request, Long id);
    void deleteTercero(Long id);

}
