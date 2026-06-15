package com.utn.tp.prog3.backend.repository;

import com.utn.tp.prog3.backend.model.Facultad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacultadRepository extends JpaRepository<Facultad, Long> {

    Page<Facultad> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
    Page<Facultad> findByDireccionContainingIgnoreCase(String direccion, Pageable pageable);
    Page<Facultad> findByCuitContainingIgnoreCase(String cuit, Pageable pageable);
    Page<Facultad> findByTelefonoContainingIgnoreCase(String telefono, Pageable pageable);
    Page<Facultad> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    Optional<Facultad> findByCuit(String cuit);

}
