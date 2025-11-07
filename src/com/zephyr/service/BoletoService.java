package com.zephyr.service;

import com.zephyr.domain.PasajeroPorVuelo;

import java.util.List;

public interface BoletoService {
    List<PasajeroPorVuelo> getPasajerosPorVuelo(int idVuelo);
    String verificarBoleto(String codigoBoleto);
}
