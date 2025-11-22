package com.zephyr.repository;

import com.zephyr.domain.ModeloAvion;

import java.util.List;

public interface ModeloAvionRepository {
    //TODO: SOLO LECTURA
    List<ModeloAvion> findAll();
}