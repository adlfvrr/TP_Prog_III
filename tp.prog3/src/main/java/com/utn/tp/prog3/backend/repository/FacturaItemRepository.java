package com.utn.tp.prog3.backend.repository;

import com.utn.tp.prog3.backend.model.FacturaItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacturaItemRepository extends JpaRepository<FacturaItem, Long> {

    List<FacturaItem> findByFacturaId(Long facturaId);

}
