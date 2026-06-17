package com.utn.tp.prog3.backend.service.implementation;

import com.utn.tp.prog3.backend.dto.request.AddPagoRequest;
import com.utn.tp.prog3.backend.dto.response.CompletePagoResponse;
import com.utn.tp.prog3.backend.dto.response.PagoDetalleResponse;
import com.utn.tp.prog3.backend.dto.response.*;
import com.utn.tp.prog3.backend.exception.ResourceNotFoundException;
import com.utn.tp.prog3.backend.model.Pago;
import com.utn.tp.prog3.backend.model.PagoDetalle;
import com.utn.tp.prog3.backend.repository.PagoDetalleRepository;
import com.utn.tp.prog3.backend.repository.PagoRepository;
import com.utn.tp.prog3.backend.repository.TerceroRepository;
import com.utn.tp.prog3.backend.service.Iservices.IPagoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@AllArgsConstructor
public class PagoServiceImpl implements IPagoService {

    private PagoRepository pagoRepository;
    private PagoDetalleRepository pagoDetalleRepository;
    private TerceroRepository terceroRepository;

    //Método mapper

    private PagoDetalleResponse mapToDetalleResponse(PagoDetalle entity){
        return entity != null ? new PagoDetalleResponse(
                entity.getId(),
                entity.getInstrumentNumber(),
                entity.getInstrumentDate(),
                entity.getBanco(),
                entity.isPagoRealizado(),
                entity.getPago().getId()
        ) : null;
    }

    @Override
    public Page<CompletePagoResponse> findAllComplete(String cuit, String modoPago, Date fechaPago, Pageable pageable) {
        Page<Pago> pagoPage;

        if(cuit != null && !cuit.isEmpty()){
            pagoPage = this.pagoRepository.findByTerceroCuitContainingIgnoreCase(cuit, pageable);
        }
        else if(modoPago != null && !modoPago.isEmpty()){
            pagoPage = this.pagoRepository.findByModoPagoContainingIgnoreCase(modoPago, pageable);
        }
        else if(fechaPago != null){
            pagoPage = this.pagoRepository.findByFechaPago(fechaPago, pageable);
        }
        else{
            pagoPage = this.pagoRepository.findAll(pageable);
        }

        return pagoPage.map(pago -> {
            PagoDetalleResponse detalleResponse = this.mapToDetalleResponse(this.pagoDetalleRepository.findByPagoId(pago.getId()));
            return new CompletePagoResponse(
                    pago.getId(),
                    pago.getTercero().getId(),
                    pago.getFechaPago(),
                    pago.getMontoPago(),
                    pago.getModoPago(),
                    detalleResponse
            );
        });
    }

    @Override
    public CompletePagoResponse findByIdComplete(Long idPago) {
        if(idPago == null || idPago <= 0){
            throw new IllegalArgumentException("El id no puede ser nulo/menor o igual a cero");
        }

        Pago p = this.pagoRepository.findById(idPago)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con id " + idPago));

        PagoDetalleResponse detalleResponse = this.mapToDetalleResponse(this.pagoDetalleRepository.findByPagoId(idPago));

        return new CompletePagoResponse(
                p.getId(),
                p.getTercero().getId(),
                p.getFechaPago(),
                p.getMontoPago(),
                p.getModoPago(),
                detalleResponse
        );
    }

    @Transactional
    @Override
    public CompletePagoResponse addPago(AddPagoRequest request) {

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
        p.setFechaPago(request.getFecha_pago());
        p.setModoPago(request.getModo_pago());
        p.setMontoPago(request.getMonto_pago());
        p.setTercero(this.terceroRepository.findById(request.getId_tercero())
                .orElseThrow(() -> new ResourceNotFoundException("Tercero no encontrado con id " + request.getId_tercero())));
        Pago savedEntity = this.pagoRepository.save(p); //Persistimos para obtener id

        //Verificamos
        if(request.getDetalleRequest().getInstrumentDate() == null){
            throw new IllegalArgumentException("El instrument date es obligatorio");
        }
        if(request.getDetalleRequest().getInstrumentNumber() == null){
            throw new IllegalArgumentException("El instrument number es obligatorio");
        }
        //Asignamos los detalles y el id
        pDetalle.setPago(savedEntity);
        pDetalle.setPagoRealizado(request.getDetalleRequest().isPagoRealizado());
        pDetalle.setBanco(request.getDetalleRequest().getBanco());
        pDetalle.setInstrumentDate(request.getDetalleRequest().getInstrumentDate());
        pDetalle.setInstrumentNumber(request.getDetalleRequest().getInstrumentNumber());
        this.pagoDetalleRepository.save(pDetalle);

        return new CompletePagoResponse(savedEntity.getId(),
                savedEntity.getTercero().getId(),
                savedEntity.getFechaPago(),
                savedEntity.getMontoPago(),
                savedEntity.getModoPago(),
                this.mapToDetalleResponse(pDetalle)
                );

    }

    @Override
    public void deletePago(Long id) {
        if(id == null || id <= 0){
            throw new IllegalArgumentException("El id no puede ser nulo/menor o igual a cero");
        }
        this.pagoRepository.deleteById(id);
    }
}
