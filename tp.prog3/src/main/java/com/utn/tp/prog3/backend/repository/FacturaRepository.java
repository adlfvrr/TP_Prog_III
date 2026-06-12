package com.utn.tp.prog3.backend.repository;

import com.utn.tp.prog3.backend.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacturaRepository extends JpaRepository<Factura, Long> {

    Optional<Factura> findByNumero(int numero);
}
