package com.utn.tp.prog3.backend.repository;

import com.utn.tp.prog3.backend.model.Facultad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacultadRepository extends JpaRepository<Facultad, Long> {

    Optional<Facultad> findByCuit(String cuit);

}
