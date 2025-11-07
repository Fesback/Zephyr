package com.zephyr.service.impl;

import com.zephyr.domain.Vuelo;
import com.zephyr.repository.VueloRepository;
import com.zephyr.service.VueloService;

import java.util.List;

public class VueloServiceImpl implements VueloService {

    private final VueloRepository vueloRepository;

    public VueloServiceImpl(VueloRepository vueloRepository) {
        this.vueloRepository = vueloRepository;
    }

    @Override
    public List<Vuelo> getVuelosProgramadosHoy() {
        return vueloRepository.findVuelosDetalladosDelDia();
    }

    @Override
    public void actualizarEstadoVuelo(int idVuelo, int idNuevoEstado) {
        System.out.println("[Debug Service] peticion para actualizar estado del vuelo ID: " + idVuelo);
        vueloRepository.actualizarEstadoVuelo(idVuelo, idNuevoEstado);
    }
}
