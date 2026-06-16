package com.utn.tp.prog3.backend.service.Iservices;

import com.utn.tp.prog3.backend.dto.request.AddFacturaRequest;
import com.utn.tp.prog3.backend.dto.response.CompleteFacturaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface IFacturaService {

    Page<CompleteFacturaResponse> findAllComplete(Integer numero, String cuit, Date fechaFactura, Pageable pageable);
    CompleteFacturaResponse findByIdComplete(Long id);
    CompleteFacturaResponse addFactura(AddFacturaRequest request);
    // No tiene sentido un update de facturas, puesto que en la cotidianidad una factura mal emitida directamente se anula, no se modifica
    void deleteFactura(Long id);


}
