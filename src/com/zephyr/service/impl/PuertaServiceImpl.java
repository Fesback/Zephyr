package com.zephyr.service.impl;

import com.zephyr.domain.PuertaEmbarque;
import com.zephyr.repository.PuertaRepository;
import com.zephyr.repository.impl.PuertaRepositoryJDBCImpl;
import com.zephyr.service.PuertaService;

import java.util.List;

public class PuertaServiceImpl implements PuertaService {

    private final PuertaRepository puertaRepository;

    public PuertaServiceImpl() {
        this.puertaRepository = new PuertaRepositoryJDBCImpl();
    }

    @Override
    public List<PuertaEmbarque> obtenerPuertasDisponibles(int idAeropuerto) {
        return puertaRepository.listarPorAeropuerto(idAeropuerto);
    }

    @Override
    public void asignarPuertaAVuelo(int idVuelo, int idPuerta) {
        puertaRepository.asignarPuerta(idVuelo, idPuerta);
    }
}
