package com.zephyr.repository;

import com.zephyr.domain.Pasajero;

import java.util.Optional;

public interface PasajeroRepository {
    Optional<Pasajero> findById(int idPasajero);
}
