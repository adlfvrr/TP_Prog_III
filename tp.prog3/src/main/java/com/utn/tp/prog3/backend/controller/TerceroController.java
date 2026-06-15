package com.utn.tp.prog3.backend.controller;

import com.utn.tp.prog3.backend.dto.request.AddTerceroRequest;
import com.utn.tp.prog3.backend.dto.request.UpdateTerceroRequest;
import com.utn.tp.prog3.backend.dto.response.TerceroResponse;
import com.utn.tp.prog3.backend.service.Iservices.ITerceroService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/tp/terceros")
@AllArgsConstructor
public class TerceroController {

    private final ITerceroService terceroService;

    @GetMapping
    public ResponseEntity<Page<TerceroResponse>> obtenerTodosTerceros(@RequestParam(required = false) String nombre,
                                                                      @RequestParam(required = false) String cuit,
                                                                      @RequestParam(required = false) String sitIVA,
                                                                      @RequestParam(required = false) String direccion,
                                                                      @RequestParam(required = false) String localidad,
                                                                      @RequestParam(required = false) String provincia,
                                                                      @RequestParam(required = false) String telefono,
                                                                      @RequestParam(required = false) String tipoSaldo,
                                                                      @PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(terceroService.findAll(nombre, cuit, sitIVA, direccion, localidad, provincia, telefono, tipoSaldo, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TerceroResponse> obtenerTerceroPorId(@PathVariable Long id){
        return ResponseEntity.ok(terceroService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Solo usuarios con rol ADMIN pueden crear terceros
    public ResponseEntity<TerceroResponse> crearTercero(@RequestBody AddTerceroRequest request) {
        return ResponseEntity.ok(terceroService.addTercero(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TerceroResponse> actualizarTercero(@RequestBody UpdateTerceroRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(terceroService.updateTercero(request, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void borrarTercero(@PathVariable Long id){
        terceroService.deleteTercero(id);
    }

}
