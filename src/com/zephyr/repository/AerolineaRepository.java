package com.zephyr.repository;

import com.zephyr.domain.Aerolinea;

import java.util.List;
import java.util.Optional;

public interface AerolineaRepository {
    //create
    void save(Aerolinea aerolinea);
    //read
    List<Aerolinea> findAll();
    Optional<Aerolinea> findById(int id);
    //update
    void update(Aerolinea aerolinea);
    //delete
    void delete(int id);
}
