package com.utn.tp.prog3.backend.service.Iservices;

import com.utn.tp.prog3.backend.dto.request.AddPagoRequest;
import com.utn.tp.prog3.backend.dto.response.CompletePagoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface IPagoService {

    Page<CompletePagoResponse> findAllComplete(String cuit, String modoPago, Date fechaPago, Pageable pageable);
    CompletePagoResponse findByIdComplete(Long idPago);
    CompletePagoResponse addPago(AddPagoRequest request);
    void deletePago(Long id);

}
