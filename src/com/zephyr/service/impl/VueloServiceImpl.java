package com.zephyr.service.impl;

import com.zephyr.domain.Vuelo;
import com.zephyr.domain.VueloRegistro;
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

    @Override
    public void registrarVuelo(VueloRegistro vuelo) {
        if (vuelo.getFechaSalida().isAfter(vuelo.getFechaLlegada())) {
            throw new IllegalArgumentException("La fecha de salida no puede ser posterior a la fecha de llegada.");
        }
        if (vuelo.getIdAeropuertoOrigen() == vuelo.getIdAeropuertoDestino()) {
            throw new IllegalArgumentException("El origen y el destiuno no pueden ser el mismo aeropuerto.");
        }
        vueloRepository.registrarVuelo(vuelo);
    }

    @Override
    public List<Vuelo> listarTodosLosVuelos() {
        return vueloRepository.findAll();
    }
}
