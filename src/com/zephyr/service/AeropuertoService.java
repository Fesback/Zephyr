package com.zephyr.service;

import com.zephyr.domain.Aeropuerto;

import java.util.List;
import java.util.Optional;

public interface AeropuertoService {
    void registrarAeropuerto(Aeropuerto aeropuerto);
    List<Aeropuerto> listarTodos();
    Optional<Aeropuerto> buscarPorId(int id);
    void actualizarAeropuerto(Aeropuerto aeropuerto);
    void eliminarAeropuerto(int id);
}
