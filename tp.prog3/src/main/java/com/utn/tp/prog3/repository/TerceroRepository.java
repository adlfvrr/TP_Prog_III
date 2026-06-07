package com.utn.tp.prog3.repository;

import com.utn.tp.prog3.model.Tercero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TerceroRepository extends JpaRepository<Tercero, Long> {

    Optional<Tercero> findByCuit(String cuit);

}
