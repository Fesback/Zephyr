package com.zephyr.repository;

import com.zephyr.domain.Aeropuerto;

import java.util.List;
import java.util.Optional;

public interface AeropuertoRepository {
    // (C) Create
    void save(Aeropuerto aeropuerto);
    // (R) Read
    List<Aeropuerto> findAll();
    Optional<Aeropuerto> findById(int id);
    // (U) Update
    void update(Aeropuerto aeropuerto);
    // (D) Delete
    void delete(int id);
}
