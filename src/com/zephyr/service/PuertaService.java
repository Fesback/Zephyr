package com.zephyr.service;

import com.zephyr.domain.PuertaEmbarque;

import java.util.List;

public interface PuertaService {
    List<PuertaEmbarque> obtenerPuertasDisponibles(int idAeropuerto);
    void asignarPuertaAVuelo(int idVuelo, int idPuerta);
}
