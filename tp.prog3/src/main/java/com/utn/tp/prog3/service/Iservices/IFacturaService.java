package com.utn.tp.prog3.service.Iservices;

import com.utn.tp.prog3.dto.request.AddFacturaRequest;
import com.utn.tp.prog3.dto.response.CompleteFacturaResponse;
import com.utn.tp.prog3.dto.response.FacturaResponse;

import java.util.List;

public interface IFacturaService {

    List<FacturaResponse> findAll();
    List<CompleteFacturaResponse> findAllComplete();
    CompleteFacturaResponse findByIdComplete(Long id);
    FacturaResponse findById(Long id);
    FacturaResponse addFactura(AddFacturaRequest request);
    // No tiene sentido un update de facturas, puesto que en la cotidianidad una factura mal emitida directamente se anula, no se modifica
    void deleteFactura(Long id);


}
