package com.utn.tp.prog3.backend.service.Iservices;

import com.utn.tp.prog3.backend.dto.request.AddPagoRequest;
import com.utn.tp.prog3.backend.dto.response.CompletePagoResponse;
import com.utn.tp.prog3.backend.dto.response.PagoResponse;

import java.util.List;

public interface IPagoService {

    List<PagoResponse> findAll();
    List<CompletePagoResponse> findAllComplete();
    CompletePagoResponse findByIdComplete(Long idPago);
    PagoResponse findById(Long id);
    PagoResponse addPago(AddPagoRequest request);
    void deletePago(Long id);

}
