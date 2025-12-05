package com.zephyr.repository;

import com.zephyr.domain.AsignacionPersonal;

import java.util.List;

public interface AsignacionRepository {
    void asignarPersonal(AsignacionPersonal asignacion);
    List<AsignacionPersonal> listarPorVuelo(int idVuelo);
}
