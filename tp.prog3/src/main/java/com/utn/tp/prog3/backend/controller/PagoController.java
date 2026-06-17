package com.utn.tp.prog3.backend.controller;

import com.utn.tp.prog3.backend.dto.request.AddPagoRequest;
import com.utn.tp.prog3.backend.dto.response.CompletePagoResponse;
import com.utn.tp.prog3.backend.service.Iservices.IPagoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/tp/pagos")
@AllArgsConstructor
public class PagoController {

    private final IPagoService pagoService;

    @GetMapping
    public ResponseEntity<Page<CompletePagoResponse>> obtenerTodosPagos(@RequestParam(required = false) String cuit,
                                                                        @RequestParam(required = false) String modoPago,
                                                                        @RequestParam(required = false) Date fechaPago,
                                                                        @PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(pagoService.findAllComplete(cuit, modoPago, fechaPago, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompletePagoResponse> obtenerPagoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.findByIdComplete(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CompletePagoResponse> crearPago(@RequestBody AddPagoRequest request) {
        return ResponseEntity.ok(pagoService.addPago(request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void borrarPago(@PathVariable Long id) {
        pagoService.deletePago(id);
    }

}
