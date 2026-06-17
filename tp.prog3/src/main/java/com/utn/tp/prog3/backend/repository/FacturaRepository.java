package com.utn.tp.prog3.backend.repository;

import com.utn.tp.prog3.backend.model.Factura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface FacturaRepository extends JpaRepository<Factura, Long> {

    Page<Factura> findByNumero(Integer numero, Pageable pageable);
    Page<Factura> findByTerceroCuitContainingIgnoreCase(String cuit, Pageable pageable);
    Page<Factura> findByFechaFactura(Date fecha_Factura, Pageable pageable);

    Optional<Factura> findByNumero(int numero);
}
