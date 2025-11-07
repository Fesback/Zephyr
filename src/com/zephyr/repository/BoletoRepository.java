package com.zephyr.repository;

import com.zephyr.domain.Boleto;
import com.zephyr.domain.PasajeroPorVuelo;

import java.util.List;
import java.util.Optional;

public interface BoletoRepository {
    List<PasajeroPorVuelo> findPasajerosByVueloId(int idVuelo);
    String verificarPasajeroEnPuerta(int idBoleto);
    Optional<Boleto> findBoletoByCodigo(String codigoBoleto);
}
