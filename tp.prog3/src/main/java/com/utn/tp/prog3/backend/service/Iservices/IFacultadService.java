package com.utn.tp.prog3.backend.service.Iservices;

import com.utn.tp.prog3.backend.dto.request.AddFacultadRequest;
import com.utn.tp.prog3.backend.dto.request.UpdateFacultadRequest;
import com.utn.tp.prog3.backend.dto.response.FacultadResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IFacultadService {

    Page<FacultadResponse> findAll(String nombre, String direccion, String cuit, String telefono, String email, Pageable pageable);
    FacultadResponse addFacultad(AddFacultadRequest request);
    FacultadResponse updateFacultad(UpdateFacultadRequest request, Long id);
    FacultadResponse findById(Long id);
    void deleteFacultad(Long id);


}
