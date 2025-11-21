package com.zephyr.service.impl;

import com.zephyr.domain.Aerolinea;
import com.zephyr.repository.AerolineaRepository;
import com.zephyr.repository.impl.AerolineaJDBCImpl;
import com.zephyr.service.AerolinaService;

import java.util.List;
import java.util.Optional;

public class AerolineaServiceImpl implements AerolinaService {

    private final AerolineaRepository aerolineaRepository;

    public AerolineaServiceImpl() {
        this.aerolineaRepository = new AerolineaJDBCImpl();
    }

    @Override
    public void registrarAerolinea(Aerolinea aerolinea) {
        //TODO: Seria buena idea validar que el codigo IATA no este dublicado
        aerolineaRepository.save(aerolinea);
    }

    @Override
    public List<Aerolinea> listarTodas() {
        return aerolineaRepository.findAll();
    }

    @Override
    public void actualizarAerolinea(Aerolinea aerolinea) {
        aerolineaRepository.update(aerolinea);
    }

    @Override
    public void eliminarAerolinea(int id) {
        //TODO: Creo que seria buena idea que antes de eliminar podamos verificar que la aerolinea no tenga vuelos asignados
        aerolineaRepository.delete(id);
    }

    @Override
    public Optional<Aerolinea> buscarPorId(int id) {
        return aerolineaRepository.findById(id);
    }
}
