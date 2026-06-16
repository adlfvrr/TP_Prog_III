package com.utn.tp.prog3.backend.controller;

import com.utn.tp.prog3.backend.dto.request.AddFacturaRequest;
import com.utn.tp.prog3.backend.dto.response.CompleteFacturaResponse;
import com.utn.tp.prog3.backend.service.Iservices.IFacturaService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/tp/facturas")
@AllArgsConstructor
public class FacturaController {

    private final IFacturaService facturaService;

    @GetMapping
    public ResponseEntity<Page<CompleteFacturaResponse>> obtenerTodasFacturas(@RequestParam(required = false) Integer numero,
                                                                              @RequestParam(required = false) String cuit,
                                                                              @RequestParam(required = false) Date fechaFactura,
                                                                              @PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(facturaService.findAllComplete(numero, cuit, fechaFactura, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompleteFacturaResponse> obtenerFacturaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.findByIdComplete(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Solo usuarios con rol ADMIN pueden crear terceros
    public ResponseEntity<CompleteFacturaResponse> crearFactura(@RequestBody AddFacturaRequest request) {
        return ResponseEntity.ok(facturaService.addFactura(request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void borrarFactura(@PathVariable Long id) {
        facturaService.deleteFactura(id);
    }

}
