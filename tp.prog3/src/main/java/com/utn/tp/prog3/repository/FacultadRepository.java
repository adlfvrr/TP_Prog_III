package com.utn.tp.prog3.repository;

import com.utn.tp.prog3.model.Facultad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacultadRepository extends JpaRepository<Facultad, Long> {

    public Optional<Facultad> findByCuit(String cuit);

}
