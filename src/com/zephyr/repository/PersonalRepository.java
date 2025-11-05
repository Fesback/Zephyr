package com.zephyr.repository;

import com.zephyr.domain.Personal;

import java.util.Optional;

public interface PersonalRepository {
    Optional<Personal> findbyCorreo(String correo);

    // luego poner metodos
}
