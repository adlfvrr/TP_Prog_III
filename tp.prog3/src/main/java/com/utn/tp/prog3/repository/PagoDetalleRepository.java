package com.utn.tp.prog3.repository;

import com.utn.tp.prog3.model.Pago;
import com.utn.tp.prog3.model.PagoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PagoDetalleRepository extends JpaRepository<PagoDetalle, Long> {

    PagoDetalle findByPagoId(Long id);


}
