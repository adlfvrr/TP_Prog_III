package com.utn.tp.prog3.service.Iservices;

import com.utn.tp.prog3.dto.request.AddTerceroRequest;
import com.utn.tp.prog3.dto.request.UpdateTerceroRequest;
import com.utn.tp.prog3.dto.response.TerceroResponse;

import java.util.List;

public interface ITerceroService {

    List<TerceroResponse> findAll();
    TerceroResponse findById(Long id);
    TerceroResponse addTercero(AddTerceroRequest request);
    TerceroResponse updateTercero(UpdateTerceroRequest request, Long id);
    void deleteTercero(Long id);

}
