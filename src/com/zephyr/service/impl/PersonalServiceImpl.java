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
    public void registrarNuevoAgente(String nombres, String apellidos, String correo, String contrasena) {
        Rol rolAgente = new Rol(3, "Agente de puerta", "Rol de agente");

        Personal nuevoAgente = new Personal(
                0,
                nombres,
                apellidos,
                correo,
                contrasena,
                rolAgente
        );

        personalRepository.save(nuevoAgente);
        System.out.println("[Debug service] Logica de registro de agente ejecutada");
    }

    @Override
    public List<Personal> listarTodoElPersonal() {
        System.out.println("[Debug service] Listando todo el personal...");
        return personalRepository.findAll();
    }

    @Override
    public void actualizarInformacionAgente(int id, String nuevoCorreo, String nuevoTurno) {
        System.out.println("[Debug service] Actualizando informacion de agente ID: ..." + id);

        Optional<Personal> personalOptional = personalRepository.findbyId(id);
        if (personalOptional.isPresent()) {
            Personal personal = personalOptional.get();
            personal.setCorreo(nuevoCorreo);
            personal.setTurno(nuevoTurno);
            personalRepository.update(personal);
            System.out.println("[Debug service] Agente fue actualizado exitosamente");

        } else {
            System.out.println("[Debug service] Agente no encontrado");
        }
    }

    @Override
    public void eliminarAgente(int id) {
        System.out.println("[Debug service] Eliminando agente ID: ..." + id);
        personalRepository.delete(id);
    }
}
