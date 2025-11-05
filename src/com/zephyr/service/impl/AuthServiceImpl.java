package com.zephyr.service.impl;

import com.zephyr.domain.Personal;
import com.zephyr.repository.PersonalRepository;
import com.zephyr.service.AuthService;

import java.util.Optional;

public class AuthServiceImpl implements AuthService {

    private final PersonalRepository personalRepository;

    public AuthServiceImpl(PersonalRepository personalRepository) {
        this.personalRepository = personalRepository;
    }

    @Override
    public Optional<Personal> login(String correo, String contrasena) {
        Optional<Personal> personalOptional = personalRepository.findbyCorreo(correo);

        if (personalOptional.isEmpty()) {
            return Optional.empty();
        }

        Personal personal = personalOptional.get();

        if (personal.getContrasenaHash().equals(contrasena)) {
            return Optional.of(personal);
        } else {
            return Optional.empty();
        }
    }
}
