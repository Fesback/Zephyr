package com.zephyr.repository;

import com.zephyr.domain.Personal;

import java.util.List;
import java.util.Optional;

public interface PersonalRepository {
    //TODO: READ (UNO)
    Optional<Personal> findbyCorreo(String correo);
    //TODO: CREATE
    void save(Personal personal);
    //TODO: READ (UNO)
    Optional<Personal> findbyId(int id);
    //TODO: READ (TODOS)
    List<Personal> findAll();
    //TODO: UPDATE
    void update(Personal personal);
    //TODO: DELETE
    void delete(int id);

    // luego poner metodos
}
