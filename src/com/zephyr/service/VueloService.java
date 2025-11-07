package com.zephyr.service;

import com.zephyr.domain.Vuelo;

import java.util.List;

public interface VueloService {
    List<Vuelo> getVuelosProgramadosHoy();
    void actualizarEstadoVuelo(int idVuelo, int idNuevoEstado);
}
