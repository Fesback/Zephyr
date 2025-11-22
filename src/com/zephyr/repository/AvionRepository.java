package com.zephyr.repository;

import com.zephyr.domain.Avion;

import java.util.List;

public interface AvionRepository {
    void save(Avion avion);
    List<Avion> findAll();
    void update(Avion avion);
    void delete(int id);
}
