package com.zephyr.service;

import com.zephyr.domain.Vuelo;
import com.zephyr.domain.VueloRegistro;

import java.util.List;

public interface VueloService {
    List<Vuelo> getVuelosProgramadosHoy();
    void actualizarEstadoVuelo(int idVuelo, int idNuevoEstado);

    void registrarVuelo(VueloRegistro vuelo);
    List<Vuelo> listarTodosLosVuelos();
}
