package com.zephyr.service;

import com.zephyr.domain.Avion;

import java.util.List;

public interface AvionService {
    void registrarAvion(Avion avion);
    List<Avion> listarTodos();
    void actualizarAvion(Avion avion);
    void eliminarAvion(int id);
}
