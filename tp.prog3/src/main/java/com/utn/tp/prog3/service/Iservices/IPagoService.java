package com.utn.tp.prog3.service.Iservices;

import com.utn.tp.prog3.dto.request.AddFacturaRequest;
import com.utn.tp.prog3.dto.request.AddPagoRequest;
import com.utn.tp.prog3.dto.response.CompleteFacturaResponse;
import com.utn.tp.prog3.dto.response.CompletePagoResponse;
import com.utn.tp.prog3.dto.response.FacturaResponse;
import com.utn.tp.prog3.dto.response.PagoResponse;

import java.util.List;

public interface IPagoService {

    List<PagoResponse> findAll();
    List<CompletePagoResponse> findAllComplete();
    CompletePagoResponse findByIdComplete(Long idPago);
    PagoResponse findById(Long id);
    PagoResponse addPago(AddPagoRequest request);
    void deletePago(Long id);

}
