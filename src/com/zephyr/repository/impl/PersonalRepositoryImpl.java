package com.zephyr.repository.impl;

import com.zephyr.domain.Personal;
import com.zephyr.domain.Rol;
import com.zephyr.repository.PersonalRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonalRepositoryImpl implements PersonalRepository {

    // Esta version es simulada,, no usara la base de datos dee postgre, usara un arraylist

    private List<Personal> baseDeDatosSimulada;

    public PersonalRepositoryImpl(){
        this.baseDeDatosSimulada = new ArrayList<>();

        Rol rolAdmin = new Rol(1, "Administrador", "Rol de admin");
        Rol rolSupervisor = new Rol(2, "Supervisro de Operaciones", "Rol de Supervisor");
        Rol rolAgente = new Rol(3, "Agente de Puerta", "Rol de agente");

        // Creare users de prueb
        Personal admin = new Personal(1, "Admin", "Zephyr", "admin@zephyr.com", "admin123", rolAdmin);
        Personal supervisor = new Personal(2, "Sara", "Connor", "s.connor@zephyr.com", "super123", rolSupervisor);
        Personal agente = new Personal(3, "Michael", "Scott", "m.scott@zephyr.com",  "agente123", rolAgente);

        // Arrays
        this.baseDeDatosSimulada.add(admin);
        this.baseDeDatosSimulada.add(supervisor);
        this.baseDeDatosSimulada.add(agente);
    }


    @Override
    public Optional<Personal> findbyCorreo(String correo) {
        for (Personal personal : this.baseDeDatosSimulada) {
            if(personal.getCorreo().equalsIgnoreCase(correo)){
                return Optional.of(personal);
            }
        }
        return Optional.empty();
    }
}
