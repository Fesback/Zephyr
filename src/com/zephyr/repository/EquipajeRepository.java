package com.zephyr.repository;

import com.zephyr.domain.Equipaje;

import java.util.List;
import java.util.Optional;

public interface EquipajeRepository {
    void save(Equipaje equipaje);
    List<Equipaje> findByBoletoId(int idBoleto);
    Optional<Equipaje> findById(int id);
}
