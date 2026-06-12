package com.utn.tp.prog3.backend.repository;

import com.utn.tp.prog3.backend.model.PagoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagoDetalleRepository extends JpaRepository<PagoDetalle, Long> {

    PagoDetalle findByPagoId(Long id);


}
