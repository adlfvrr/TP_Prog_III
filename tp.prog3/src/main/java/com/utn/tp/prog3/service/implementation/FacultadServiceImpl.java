package com.utn.tp.prog3.service.implementation;

import com.utn.tp.prog3.exception.EntityAlreadyExistsException;
import com.utn.tp.prog3.exception.ResourceNotFoundException;
import com.utn.tp.prog3.dto.request.AddFacultadRequest;
import com.utn.tp.prog3.dto.request.UpdateFacultadRequest;
import com.utn.tp.prog3.dto.response.FacultadResponse;
import com.utn.tp.prog3.model.Facultad;
import com.utn.tp.prog3.repository.FacultadRepository;
import com.utn.tp.prog3.service.Iservices.IFacultadService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class FacultadServiceImpl implements IFacultadService {

    private FacultadRepository facultadRepository;

    //Método mapper
    private FacultadResponse mapToResponse(Facultad entity){
        return new FacultadResponse(
                entity.getId_facultad(),
                entity.getNombre(),
                entity.getDireccion(),
                entity.getCuit(),
                entity.getSucursal(),
                entity.getTelefono(),
                entity.getEmail(),
                entity.isDefecto()
        );
    }

    @Override
    public List<FacultadResponse> findAll() {
        return this.facultadRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public FacultadResponse findById(Long id){
        if(id == null || id <= 0){
            throw new IllegalArgumentException("El id no puede ser nulo o menor/igual a cero");
        }

        return this.mapToResponse(this.facultadRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No se encontró la facultad buscada")));
    }

    public FacultadResponse addFacultad(AddFacultadRequest request){
        //Verificamos primero si no existe por el CUIT

        //Optional en el repository nos permite utilizar el método isPresent() para verificar la existencia de una entidad
        if(this.facultadRepository.findByCuit(request.getCuit()).isPresent()){
            throw new EntityAlreadyExistsException("Ya existe una facultad con CUIT " + request.getCuit());
        }

        //Verificaciones extra de servicio (ya hechas con Bean Validation, pero reforzamos)
        if(request.getNombre() == null || request.getNombre().isBlank()){
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if(request.getCuit() == null || request.getCuit().isBlank()){
            throw new IllegalArgumentException("El CUIT es obligatorio");
        }
        if(request.getDireccion() == null || request.getDireccion().isBlank()){
            throw new IllegalArgumentException("La dirección es obligatoria");
        }

        //Si no existe, la creamos (Nos salteamos el id ya que se autogenerará al momento de agregar a la bdd)
        Facultad f = new Facultad();
        f.setNombre(request.getNombre());
        f.setCuit(request.getCuit());
        f.setEmail(request.getEmail());
        f.setDireccion(request.getDireccion());
        f.setTelefono(request.getTelefono());
        f.setSucursal(request.getSucursal());
        f.setDefecto(request.isDefecto());
        //Guardamos
        return this.mapToResponse(facultadRepository.save(f));
    }

    public FacultadResponse updateFacultad(UpdateFacultadRequest request, Long id){

        if(id == null || id <= 0){
            throw new IllegalArgumentException("El id no puede ser nulo o menor/igual a cero");
        }

        Facultad f = this.facultadRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No se encontró la facultad a actualizar"));

        //Según los datos presentes, modificamos
        if(!request.getNombre().isBlank()){
            f.setNombre(request.getNombre());
        }
        if(!request.getEmail().isBlank()){
            f.setEmail(request.getEmail());
        }
        if(!request.getCuit().isBlank()){
            f.setCuit(request.getCuit());
        }
        if(!request.getTelefono().isBlank()){
            f.setTelefono(request.getTelefono());
        }
        if(request.getSucursal() != null){
            f.setSucursal(request.getSucursal());
        }
        if(request.isDefecto() != f.isDefecto()){
            f.setDefecto(request.isDefecto());
        }

        return this.mapToResponse(facultadRepository.save(f));
    }

    public void deleteFacultad(Long id){
        if(id == null || id <= 0){
            throw new IllegalArgumentException("El id no puede ser nulo o menor/igual a cero");
        }
        this.facultadRepository.deleteById(id);
    }

}
