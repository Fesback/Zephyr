package com.zephyr.repository;

import com.zephyr.domain.Ciudad;

import java.util.List;
import java.util.Optional;

public interface CiudadRepository {
    void save(Ciudad ciudad);
    List<Ciudad> findAll();
    Optional<Ciudad> findById(int id);
    void update(Ciudad ciudad);
    void delete(int id);
}
