package com.zephyr.service;

import com.zephyr.domain.AsignacionPersonal;

import java.util.List;

public interface AsignacionService {
    void asignarPersonal(AsignacionPersonal asignacion);
    List<AsignacionPersonal> listarAsignacionesDeVuelo(int idVuelo);
}
