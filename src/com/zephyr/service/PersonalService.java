package com.zephyr.service;

import com.zephyr.domain.Personal;

import java.util.List;

public interface PersonalService {
    // create
    void registrarNuevoAgente(String nombres, String apellidos, String correo, String contrasena);
    // read all
    List<Personal> listarTodoElPersonal();
    // update
    void actualizarInformacionAgente (int id, String nuevoCorreo, String nuevoTurno);
    // delete
    void eliminarAgente(int id);
}
