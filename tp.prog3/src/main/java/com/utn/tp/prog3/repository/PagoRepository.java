package com.utn.tp.prog3.repository;

import com.utn.tp.prog3.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    Optional<Pago> findByIdPago(Long id);

}
