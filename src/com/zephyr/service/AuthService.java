package com.zephyr.service;

import com.zephyr.domain.Personal;

import java.util.Optional;

public interface AuthService {
    Optional<Personal> login(String correo, String contrasena);
}
