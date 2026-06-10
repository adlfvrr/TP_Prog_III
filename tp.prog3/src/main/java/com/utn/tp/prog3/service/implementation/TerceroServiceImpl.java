package com.utn.tp.prog3.service.implementation;

import com.utn.tp.prog3.dto.request.AddTerceroRequest;
import com.utn.tp.prog3.dto.request.UpdateTerceroRequest;
import com.utn.tp.prog3.dto.response.TerceroResponse;
import com.utn.tp.prog3.exception.EntityAlreadyExistsException;
import com.utn.tp.prog3.exception.ResourceNotFoundException;
import com.utn.tp.prog3.model.Tercero;
import com.utn.tp.prog3.repository.TerceroRepository;
import com.utn.tp.prog3.service.Iservices.ITerceroService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TerceroServiceImpl implements ITerceroService {

    private TerceroRepository terceroRepository;

    //Método mapper
    private TerceroResponse mapToResponse(Tercero entity){
        return new TerceroResponse(
                entity.getId_tercero(),
                entity.getNombre(),
                entity.getCuit(),
                entity.getSitIVA(),
                entity.getDireccion(),
                entity.getLocalidad(),
                entity.getProvincia(),
                entity.getTelefono(),
                entity.getSaldo_apertura(),
                entity.getTipo_saldo()
        );
    }

    @Override
    public List<TerceroResponse> findAll() {
        return this.terceroRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TerceroResponse findById(Long id) {
        if(id == null || id <= 0){
            throw new IllegalArgumentException("El id no puede ser nulo/menor o igual 0");
        }
        return this.mapToResponse(this.terceroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el tercero con id " + id)));
    }

    @Transactional
    @Override
    public TerceroResponse addTercero(AddTerceroRequest request) {
        //Verificamos si existe -> por CUIT
        if(this.terceroRepository.findByCuit(request.getCuitl()).isPresent()){
            throw new EntityAlreadyExistsException("El tercero ya existe con CUIT " + request.getCuitl());
        }
        if(request.getNombre() == null || request.getNombre().isBlank()){
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if(request.getDireccion() == null || request.getDireccion().isBlank()){
            throw new IllegalArgumentException("La dirección es obligatoria");
        }
        if(request.getCuitl() == null || request.getCuitl().isBlank()){
            throw new IllegalArgumentException("El CUIT es obligatoria");
        }
        if(request.getSitIVA() == null ||request.getSitIVA().isBlank()){
            throw new IllegalArgumentException("La situación IVA es obligatoria");
        }

        //Creamos
        Tercero t = new Tercero();
        t.setNombre(request.getNombre());
        t.setDireccion(request.getDireccion());
        t.setCuit(request.getCuitl());
        t.setSitIVA(request.getSitIVA());
        t.setLocalidad(request.getLocalidad());
        t.setProvincia(request.getProvincia());
        t.setSaldo_apertura(request.getSaldo_apertura());
        t.setTipo_saldo(request.getTipo_saldo());
        t.setTelefono(request.getTelefono());

        return this.mapToResponse(this.terceroRepository.save(t));

    }

    @Transactional
    @Override
    public TerceroResponse updateTercero(UpdateTerceroRequest request, Long id) {
        if(id == null || id <= 0){
            throw new IllegalArgumentException("El id no puede ser nulo/menor o igual 0");
        }
        Tercero t = this.terceroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el tercero a actualizar"));

        if(!request.getNombre().isBlank()){
            t.setNombre(request.getNombre());
        }
        if(!request.getDireccion().isBlank()){
            t.setDireccion(request.getDireccion());
        }
        if(!request.getCuitl().isBlank()){
            t.setCuit(request.getCuitl());
        }
        if(!request.getLocalidad().isBlank()){
            t.setLocalidad(request.getLocalidad());
        }
        if(!request.getProvincia().isBlank()){
            t.setProvincia(request.getProvincia());
        }
        if(!request.getSitIVA().isBlank()){
            t.setSitIVA(request.getSitIVA());
        }
        if(!request.getTelefono().isBlank()){
            t.setTelefono(request.getTelefono());
        }
        if(request.getSaldo_apertura() != 0){
            t.setSaldo_apertura(request.getSaldo_apertura());
        }
        if(!request.getTipo_saldo().isBlank()){
            t.setTipo_saldo(request.getTipo_saldo());
        }

        return this.mapToResponse(this.terceroRepository.save(t));

    }

    @Override
    public void deleteTercero(Long id) {
        if(id == null || id <= 0){
            throw new IllegalArgumentException("El id no puede ser nulo/menor o igual 0");
        }

        this.terceroRepository.deleteById(id);
    }

}
