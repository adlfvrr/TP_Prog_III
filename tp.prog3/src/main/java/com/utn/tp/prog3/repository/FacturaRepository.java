package com.utn.tp.prog3.repository;

import com.utn.tp.prog3.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FacturaRepository extends JpaRepository<Factura, Long> {

    Optional<Factura> findByIdFactura(Long idFactura);
    Optional<Factura> findByNumero(int numero);
}
