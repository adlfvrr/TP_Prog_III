package com.utn.tp.prog3.backend.controller;

import com.utn.tp.prog3.backend.dto.request.AddFacultadRequest;
import com.utn.tp.prog3.backend.dto.request.UpdateFacultadRequest;
import com.utn.tp.prog3.backend.dto.response.FacultadResponse;
import com.utn.tp.prog3.backend.service.Iservices.IFacultadService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tp/facultades")
@AllArgsConstructor
public class FacultadController {

    private final IFacultadService facultadService;

    @GetMapping
    public ResponseEntity<Page<FacultadResponse>> obtenerTodasFacultades(@RequestParam(required = false) String nombre,
                                                                         @RequestParam(required = false) String direccion,
                                                                         @RequestParam(required = false) String cuit,
                                                                         @RequestParam(required = false) String telefono,
                                                                         @RequestParam(required = false) String email,
                                                                         @PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(facultadService.findAll(nombre, direccion, cuit, telefono, email, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacultadResponse> obtenerFacultadPorId(@PathVariable Long id) {
        return ResponseEntity.ok(facultadService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FacultadResponse> crearFacultad(@RequestBody AddFacultadRequest request) {
        return ResponseEntity.ok(facultadService.addFacultad(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FacultadResponse> actualizarFacultad(@RequestBody UpdateFacultadRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(facultadService.updateFacultad(request, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void borrarFacultad(@PathVariable Long id) {
        facultadService.deleteFacultad(id);
    }

}
