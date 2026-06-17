package com.utn.tp.prog3.backend.repository;

import com.utn.tp.prog3.backend.model.Pago;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    Page<Pago> findByTerceroCuitContainingIgnoreCase(String cuit, Pageable pageable);
    Page<Pago> findByModoPagoContainingIgnoreCase(String modoPago, Pageable pageable);
    Page<Pago> findByFechaPago(Date fechaPago, Pageable pageable);

}
