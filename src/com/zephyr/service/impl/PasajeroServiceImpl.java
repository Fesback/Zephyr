package com.zephyr.service.impl;

import com.zephyr.domain.Pasajero;
import com.zephyr.repository.PasajeroRepository;
import com.zephyr.repository.impl.PasajeroRepositoryJDBCImpl;
import com.zephyr.service.PasajeroService;

import java.util.Optional;

public class PasajeroServiceImpl implements PasajeroService {

    private final PasajeroRepository pasajeroRepository;

    public PasajeroServiceImpl() {
        this.pasajeroRepository = new PasajeroRepositoryJDBCImpl();
    }

    @Override
    public Pasajero registrarPasajero(Pasajero pasajero) {
        return pasajeroRepository.save(pasajero);
    }

    @Override
    public Optional<Pasajero> findById(int id) {
        return pasajeroRepository.findById(id);
    }
}
