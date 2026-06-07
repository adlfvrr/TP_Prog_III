package com.utn.tp.prog3.service.implementation;

import com.utn.tp.prog3.dto.request.AddPagoRequest;
import com.utn.tp.prog3.dto.response.*;
import com.utn.tp.prog3.exception.ResourceNotFoundException;
import com.utn.tp.prog3.model.Factura;
import com.utn.tp.prog3.model.Pago;
import com.utn.tp.prog3.model.PagoDetalle;
import com.utn.tp.prog3.repository.PagoDetalleRepository;
import com.utn.tp.prog3.repository.PagoRepository;
import com.utn.tp.prog3.service.Iservices.IPagoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PagoServiceImpl implements IPagoService {

    private PagoRepository pagoRepository;
    private PagoDetalleRepository pagoDetalleRepository;

    //Método mapper
    private PagoResponse mapToResponse(Pago entity){
        return new PagoResponse(
                entity.getId_pagos(),
                entity.getId_tercero(),
                entity.getFecha_pago(),
                entity.getMonto_pago(),
                entity.getModo_pago()
        );
    }

    private PagoDetalleResponse mapToDetalleResponse(PagoDetalle entity){
        return new PagoDetalleResponse(
                entity.getId_pagosdetalle(),
                entity.getInstrumentNumber(),
                entity.getInstrumentDate(),
                entity.getBanco(),
                entity.isPagoRealizado(),
                entity.getId_pago()
        );
    }

    @Override
    public List<PagoResponse> findAll() {
        return this.pagoRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CompletePagoResponse> findAllComplete() {
        return this.pagoRepository.findAll().stream()
                .map(pago -> {
                    PagoDetalleResponse detalleResponse = this.mapToDetalleResponse(this.pagoDetalleRepository.findByIdPago(pago.getId_pagos()));
                    return new CompletePagoResponse(
                            pago.getId_pagos(),
                            pago.getId_tercero(),
                            pago.getFecha_pago(),
                            pago.getMonto_pago(),
                            pago.getModo_pago(),
                            detalleResponse
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public CompletePagoResponse findByIdComplete(Long idPago) {
        if(idPago == null || idPago <= 0){
            throw new IllegalArgumentException("El id no puede ser nulo/menor o igual a cero");
        }

        Pago p = this.pagoRepository.findByIdPago(idPago)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con id " + idPago));

        PagoDetalleResponse detalleResponse = this.mapToDetalleResponse(this.pagoDetalleRepository.findByIdPago(idPago));

        return new CompletePagoResponse(
                p.getId_pagos(),
                p.getId_tercero(),
                p.getFecha_pago(),
                p.getMonto_pago(),
                p.getModo_pago(),
                detalleResponse
        );
    }

    @Override
    public PagoResponse findById(Long id) {
        if(id == null || id <= 0){
            throw new IllegalArgumentException("El id no puede ser nulo/menor o igual a cero");
        }
        return this.mapToResponse(this.pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con id " + id)));
    }

    @Transactional
    @Override
    public PagoResponse addPago(AddPagoRequest request) {

        //Creamos entidades
        Pago p = new Pago();
        PagoDetalle pDetalle = new PagoDetalle();
        //Verificaciones
        if(request.getFecha_pago() == null){
            throw new IllegalArgumentException("La fecha de pago es obligatoria");
        }
        if(request.getModo_pago().isBlank()){
            throw new IllegalArgumentException("El modo de pago es obligatorio");
        }
        if(request.getDetalleRequest() == null){
            throw new IllegalArgumentException("El detalle de pago es obligatorio");
        }
        if(request.getId_tercero() == null || request.getId_tercero() <= 0){
            throw new IllegalArgumentException("El id no puede ser nulo/menor o igual a cero");
        }
        // Asignamos y persistimos
        p.setFecha_pago(request.getFecha_pago());
        p.setModo_pago(request.getModo_pago());
        p.setMonto_pago(request.getMonto_pago());
        p.setId_tercero(request.getId_tercero());
        Pago savedEntity = this.pagoRepository.save(p); //Persistimos para obtener id

        //Verificamos
        if(request.getDetalleRequest().getInstrumentDate() == null){
            throw new IllegalArgumentException("El instrument date es obligatorio");
        }
        if(request.getDetalleRequest().getInstrumentNumber() == null){
            throw new IllegalArgumentException("El instrument number es obligatorio");
        }
        //Asignamos los detalles y el id
        pDetalle.setId_pago(savedEntity.getId_pagos());
        pDetalle.setPagoRealizado(request.getDetalleRequest().isPagoRealizado());
        pDetalle.setBanco(request.getDetalleRequest().getBanco());
        pDetalle.setInstrumentDate(request.getDetalleRequest().getInstrumentDate());
        pDetalle.setInstrumentNumber(request.getDetalleRequest().getInstrumentNumber());
        this.pagoDetalleRepository.save(pDetalle);

        return this.mapToResponse(savedEntity);

    }

    @Override
    public void deletePago(Long id) {
        if(id == null || id <= 0){
            throw new IllegalArgumentException("El id no puede ser nulo/menor o igual a cero");
        }
        this.pagoRepository.deleteById(id);
    }
}
