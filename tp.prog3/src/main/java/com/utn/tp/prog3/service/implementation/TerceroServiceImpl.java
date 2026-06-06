package com.utn.tp.prog3.service.implementation;

import com.utn.tp.prog3.repository.TerceroRepository;
import com.utn.tp.prog3.service.Iservices.ITerceroService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TerceroServiceImpl implements ITerceroService {

    private TerceroRepository terceroRepository;



}
