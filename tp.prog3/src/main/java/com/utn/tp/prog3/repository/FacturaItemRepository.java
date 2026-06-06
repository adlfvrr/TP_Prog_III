package com.utn.tp.prog3.repository;

import com.utn.tp.prog3.model.FacturaItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacturaItemRepository extends JpaRepository<FacturaItem, Long> {

    List<FacturaItem> findByIdFactura(Long facturaId);

}
