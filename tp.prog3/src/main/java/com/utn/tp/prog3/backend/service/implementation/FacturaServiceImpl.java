package com.utn.tp.prog3.backend.service.implementation;

import com.utn.tp.prog3.backend.dto.request.AddFacturaItemRequest;
import com.utn.tp.prog3.backend.dto.request.AddFacturaRequest;
import com.utn.tp.prog3.backend.dto.response.CompleteFacturaResponse;
import com.utn.tp.prog3.backend.dto.response.FacturaItemResponse;
import com.utn.tp.prog3.backend.dto.response.FacturaResponse;
import com.utn.tp.prog3.backend.exception.EntityAlreadyExistsException;
import com.utn.tp.prog3.backend.exception.ResourceNotFoundException;
import com.utn.tp.prog3.backend.model.Factura;
import com.utn.tp.prog3.backend.model.FacturaItem;
import com.utn.tp.prog3.backend.repository.FacturaItemRepository;
import com.utn.tp.prog3.backend.repository.FacturaRepository;
import com.utn.tp.prog3.backend.repository.TerceroRepository;
import com.utn.tp.prog3.backend.service.Iservices.IFacturaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class FacturaServiceImpl implements IFacturaService {

    private FacturaRepository facturaRepository;
    private FacturaItemRepository facturaItemRepository;
    private TerceroRepository terceroRepository;

    //Método mapper
    private FacturaResponse mapFacturaToResponse(Factura entity){
        return new FacturaResponse(
                entity.getId(),
                entity.getFecha_factura(),
                entity.getTercero().getId(),
                entity.getNumero()
        );
    }
    private FacturaItemResponse mapFacturaItemToResponse(FacturaItem entity){
        return new FacturaItemResponse(
                entity.getId(),
                entity.getMonto(),
                entity.getCantidad(),
                entity.getFactura().getId(),
                entity.getDetalle()
        );
    }

    @Override
    public List<FacturaResponse> findAll() {
        return this.facturaRepository.findAll().stream()
                .map(this::mapFacturaToResponse)
                .collect(Collectors.toList());
    }

    //Ahora un findAll y findById que me devuelva tanto facturas como sus items

    @Override
    public List<CompleteFacturaResponse> findAllComplete(){
        return this.facturaRepository.findAll().stream()
                .map(factura -> { //Aplico lambda -un poco largo- para poder convertir los items y la factura en response y mapearlos
                    //Mapeo y obtengo lista mapeada
                    List<FacturaItemResponse> itemResponses = this.facturaItemRepository.findByFacturaId(factura.getId()).stream()
                            .map(this::mapFacturaItemToResponse)
                            .collect(Collectors.toList());
                    //Mapeo a CompleteFacturaResponse
                    return new CompleteFacturaResponse(
                            factura.getId(),
                            factura.getFecha_factura(),
                            factura.getTercero().getId(),
                            factura.getNumero(),
                            itemResponses
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public CompleteFacturaResponse findByIdComplete(Long idFactura){
        if(idFactura == null || idFactura <= 0){
            throw new IllegalArgumentException("El id no puede ser nulo/menor o igual a cero");
        }

        //Ahora hay que encontrar tanto la factura como los items y mapearlo
        Factura f = this.facturaRepository.findById(idFactura)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id " + idFactura));

        List<FacturaItemResponse> itemResponses = this.facturaItemRepository.findByFacturaId(idFactura).stream()
                .map(this::mapFacturaItemToResponse)
                .collect(Collectors.toList());

        return new CompleteFacturaResponse(
                f.getId(),
                f.getFecha_factura(),
                f.getTercero().getId(),
                f.getNumero(),
                itemResponses
        );

    }
    @Override
    public FacturaResponse findById(Long id) {
        if(id == null || id <= 0){
            throw new IllegalArgumentException("El id no puede ser nulo/menor o igual a cero");
        }
        return this.mapFacturaToResponse(this.facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id " + id)));
    }

    @Transactional
    @Override
    public FacturaResponse addFactura(AddFacturaRequest request) {

        //Comprobamos existencia de factura mediante su numero
        if(this.facturaRepository.findByNumero(request.getNumero()).isPresent()){
            throw new EntityAlreadyExistsException("La factura ya existe con número " + request.getNumero());
        }
        //Ahora debemos crear tanto la factura como los items y persistirlos
        //Verificamos que tenga items
        if(request.getItemRequestList().isEmpty()){
            throw new IllegalArgumentException("La factura debe contener al menos un item");
        }

        //Primero crearemos la factura, pues necesitamos su id para asignarselo a sus items
        Factura f = new Factura();

        //Verificaciones
        if(request.getNumero() <= 0){
            throw new IllegalArgumentException("El número de la factura debe ser mayor a 0");
        }
        if(request.getFecha_factura() == null){
            throw new IllegalArgumentException("La fecha debe ser válida");
        }
        if(request.getId_tecero() == null || request.getId_tecero() <= 0){
            throw new IllegalArgumentException("El id no puede ser nulo/menor o igual a cero");
        }

        f.setFecha_factura(request.getFecha_factura());
        f.setNumero(request.getNumero());
        f.setTercero(this.terceroRepository.findById(request.getId_tecero())
                .orElseThrow(() -> new ResourceNotFoundException("Tercero no encontrado con id " + request.getId_tecero())));
        Factura savedEntity = this.facturaRepository.save(f);

        //Ahora recorremos los items y asignamos el id
        for(AddFacturaItemRequest itemRequest : request.getItemRequestList()){
            FacturaItem item = new FacturaItem();

            //Verificaciones
            if(itemRequest.getMonto() < 0){
                throw new IllegalArgumentException("No puede haber monto negativo en facturas");
            }
            if(itemRequest.getCantidad() <= 0){
                throw new IllegalArgumentException("No puede haber cantidad negativa o igual a cero");
            }
            if(itemRequest.getDetalle().isBlank()){
                throw new IllegalArgumentException("El item debe contener detalles");
            }

            //Seteamos y persistimos
            item.setFactura(savedEntity); //Importante, asignar el id de nuestra factura guardada
            item.setCantidad(itemRequest.getCantidad());
            item.setMonto(itemRequest.getMonto());
            item.setDetalle(itemRequest.getDetalle());
            this.facturaItemRepository.save(item);
        }

        return this.mapFacturaToResponse(savedEntity);
    }

    @Override
    public void deleteFactura(Long id) {
        if(id == null || id <= 0){
            throw new IllegalArgumentException("El id no puede ser nulo/menor o igual a cero");
        }
        this.facturaRepository.deleteById(id);
    }
}
