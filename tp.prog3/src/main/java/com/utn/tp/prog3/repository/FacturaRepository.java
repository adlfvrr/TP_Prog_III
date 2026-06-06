package com.utn.tp.prog3.repository;

import com.utn.tp.prog3.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacturaRepository extends JpaRepository<Factura, Long> {

    List<Factura> findByIdTercero(Long idTercero);
}
