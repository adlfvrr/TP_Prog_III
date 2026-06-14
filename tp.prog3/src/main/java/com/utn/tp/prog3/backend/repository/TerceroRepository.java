package com.utn.tp.prog3.backend.repository;

import com.utn.tp.prog3.backend.model.SitIVA;
import com.utn.tp.prog3.backend.model.Tercero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TerceroRepository extends JpaRepository<Tercero, Long> {

    //Añadimos al repositorio los métodos necesarios para los filtrados
    Page<Tercero> findByNombreContainingIgnoreCase(String nombre, Pageable page);
    Page<Tercero> findByCuit(String cuit, Pageable page);
    Page<Tercero> findBySitIVA(SitIVA sitIVA, Pageable page);
    Page<Tercero> findByDireccionContainingIgnoreCase(String direccion, Pageable page);
    Page<Tercero> findByLocalidadContainingIgnoreCase(String localidad, Pageable page);
    Page<Tercero> findByProvinciaContainingIgnoreCase(String provincia, Pageable page);
    Page<Tercero> findByTelefono(String telefono, Pageable page);
    Page<Tercero> findByTipoSaldoContainingIgnoreCase(String tipo_saldo, Pageable page);

    Optional<Tercero> findByCuit(String cuit);
}
