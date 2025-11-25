package com.zephyr.repository;

import com.zephyr.domain.PuertaEmbarque;

import java.util.List;

public interface PuertaRepository {
    List<PuertaEmbarque> listarPorAeropuerto(int idAeropuerto);
    void asignarPuerta(int idVuelo, int idPuerta);
}
