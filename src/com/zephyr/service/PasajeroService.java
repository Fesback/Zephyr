package com.zephyr.service;

import com.zephyr.domain.Pasajero;

import java.util.Optional;

public interface PasajeroService {
    Pasajero registrarPasajero(Pasajero pasajero);
    Optional<Pasajero> findById(int id);
}
