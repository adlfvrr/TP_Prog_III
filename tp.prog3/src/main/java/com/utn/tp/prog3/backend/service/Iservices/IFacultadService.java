package com.utn.tp.prog3.backend.service.Iservices;

import com.utn.tp.prog3.backend.dto.request.AddFacultadRequest;
import com.utn.tp.prog3.backend.dto.request.UpdateFacultadRequest;
import com.utn.tp.prog3.backend.dto.response.FacultadResponse;

import java.util.List;

public interface IFacultadService {

    List<FacultadResponse> findAll();
    FacultadResponse addFacultad(AddFacultadRequest request);
    FacultadResponse updateFacultad(UpdateFacultadRequest request, Long id);
    FacultadResponse findById(Long id);
    void deleteFacultad(Long id);


}
