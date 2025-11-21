package com.zephyr.service;

import com.zephyr.domain.Aerolinea;

import java.util.List;
import java.util.Optional;

public interface AerolinaService {
    void registrarAerolinea(Aerolinea aerolinea);
    List<Aerolinea> listarTodas();
    void actualizarAerolinea(Aerolinea aerolinea);
    void eliminarAerolinea(int id);
    Optional<Aerolinea> buscarPorId(int id);
}
