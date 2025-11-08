package com.zephyr.service;

import com.zephyr.domain.Personal;

import java.util.List;
import java.util.Optional;

public interface PersonalService {
    // create
    void registrarPersonal(Personal personal);
    // read all
    List<Personal> listarTodoElPersonal();
    // update
    void actualizarPersonal (Personal personal);
    // delete
    void eliminarAgente(int id);

    Optional<Personal> findById(int id);
}
