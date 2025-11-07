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
}
