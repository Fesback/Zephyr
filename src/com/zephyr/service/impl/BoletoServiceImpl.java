package com.zephyr.service.impl;

import com.zephyr.domain.Boleto;
import com.zephyr.domain.PasajeroPorVuelo;
import com.zephyr.repository.BoletoRepository;
import com.zephyr.service.BoletoService;

import java.util.List;
import java.util.Optional;

public class BoletoServiceImpl implements BoletoService {

    private final BoletoRepository boletoRepository;

    public BoletoServiceImpl(BoletoRepository boletoRepository) {
        this.boletoRepository = boletoRepository;
    }

    @Override
    public List<PasajeroPorVuelo> getPasajerosPorVuelo(int idVuelo) {
        return boletoRepository.findPasajerosByVueloId(idVuelo);
    }

    @Override
    public String verificarBoleto(String codigoBoleto) {
        if (codigoBoleto == null || codigoBoleto.trim().isEmpty()) {
            return "ERROR: El codigo del boleto no puede estar vacio";
        }

        Optional<Boleto> boletoOptional = boletoRepository.findBoletoByCodigo(codigoBoleto);

        if (boletoOptional.isEmpty()) {
            return "ERROR: El codigo del boleto no existe en la base de datos";
        }

        int idBoleto = boletoOptional.get().getIdBoleto();

        return boletoRepository.verificarPasajeroEnPuerta(idBoleto);
    }

    @Override
    public void registrarBoleto(Boleto boleto) {
        boletoRepository.save(boleto);
    }
}
