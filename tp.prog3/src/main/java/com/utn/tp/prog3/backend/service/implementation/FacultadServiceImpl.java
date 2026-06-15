package com.utn.tp.prog3.backend.service.implementation;

import com.utn.tp.prog3.backend.exception.EntityAlreadyExistsException;
import com.utn.tp.prog3.backend.exception.ResourceNotFoundException;
import com.utn.tp.prog3.backend.dto.request.AddFacultadRequest;
import com.utn.tp.prog3.backend.dto.request.UpdateFacultadRequest;
import com.utn.tp.prog3.backend.dto.response.FacultadResponse;
import com.utn.tp.prog3.backend.model.Facultad;
import com.utn.tp.prog3.backend.repository.FacultadRepository;
import com.utn.tp.prog3.backend.service.Iservices.IFacultadService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class FacultadServiceImpl implements IFacultadService {

    private FacultadRepository facultadRepository;

    //Método mapper
    private FacultadResponse mapToResponse(Facultad entity){
        return new FacultadResponse(
                entity.getId(),
                entity.getNombre(),
                entity.getDireccion(),
                entity.getCuit(),
                entity.getSucursal(),
                entity.getTelefono(),
                entity.getEmail(),
                entity.getDefecto()
        );
    }

    @Override
    public Page<FacultadResponse> findAll(String nombre, String direccion, String cuit, String telefono, String email, Pageable pageable) {
        Page<Facultad> pageFacultad;

        if(nombre != null && !nombre.isEmpty()){
            pageFacultad = this.facultadRepository.findByNombreContainingIgnoreCase(nombre, pageable);
        }
        else if(direccion != null && !direccion.isEmpty()){
            pageFacultad = this.facultadRepository.findByDireccionContainingIgnoreCase(direccion, pageable);
        }
        else if(cuit != null && !cuit.isEmpty()){
            pageFacultad = this.facultadRepository.findByCuitContainingIgnoreCase(cuit, pageable);
        }
        else if(telefono != null && !telefono.isEmpty()){
            pageFacultad = this.facultadRepository.findByTelefonoContainingIgnoreCase(telefono, pageable);
        }
        else if(email != null && !email.isEmpty()){
            pageFacultad = this.facultadRepository.findByEmailContainingIgnoreCase(email, pageable);
        }
        else{
            pageFacultad = this.facultadRepository.findAll(pageable);
        }

        return pageFacultad.map(this::mapToResponse);
    }

    public FacultadResponse findById(Long id){
        if(id == null || id <= 0){
            throw new IllegalArgumentException("El id no puede ser nulo o menor/igual a cero");
        }

        return this.mapToResponse(this.facultadRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No se encontró la facultad buscada")));
    }

    @Transactional
    @Override
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
        f.setDefecto(request.getDefecto());
        //Guardamos
        return this.mapToResponse(facultadRepository.save(f));
    }

    @Override
    @Transactional
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
        if(request.getDefecto() != null && !request.getDefecto().equals(f.getDefecto())){
            f.setDefecto(request.getDefecto());
        }

        return this.mapToResponse(facultadRepository.save(f));
    }

    @Override
    public void deleteFacultad(Long id){
        if(id == null || id <= 0){
            throw new IllegalArgumentException("El id no puede ser nulo o menor/igual a cero");
        }
        this.facultadRepository.deleteById(id);
    }

}
