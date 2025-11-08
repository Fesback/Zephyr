package com.zephyr.service.impl;

import com.zephyr.domain.Personal;
import com.zephyr.domain.Rol;
import com.zephyr.repository.PersonalRepository;
import com.zephyr.service.PersonalService;

import java.util.List;
import java.util.Optional;

public class PersonalServiceImpl implements PersonalService {

    private final PersonalRepository personalRepository;

    public PersonalServiceImpl(PersonalRepository personalRepository) {
        this.personalRepository = personalRepository;
    }

    @Override
    public void registrarPersonal(Personal personal) {
        Rol rolAgente = new Rol(3, "Agente de puerta", "Rol de agente");

        //TODO: podria agregarle un hash a la contrasena, por seguridad

        personalRepository.save(personal);
        System.out.println("[Debug service] Logica de registro de agente ejecutada");
    }

    @Override
    public List<Personal> listarTodoElPersonal() {
        System.out.println("[Debug service] Listando todo el personal...");
        return personalRepository.findAll();
    }

    @Override
    public void actualizarPersonal(Personal personal) {
        System.out.println("[Debug service] Actualizando informacion de agente ID: ...");
        Optional<Personal> personalOptional = personalRepository.findbyId(personal.getIdPersonal());
        if (personalOptional.isPresent()) {
            personalRepository.update(personal);
            System.out.println("[Debug Service] Agente actualizado.");
        } else {
            System.out.println("[Debug Service] ERROR: No se encontró agente con ID: " + personal.getIdPersonal());
        }
    }

    @Override
    public void eliminarAgente(int id) {
        System.out.println("[Debug service] Eliminando agente ID: ..." + id);
        personalRepository.delete(id);
    }

    @Override
    public Optional<Personal> findById(int id) {
        return personalRepository.findbyId(id);
    }
}
