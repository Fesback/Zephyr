package com.zephyr.service.impl;

import com.zephyr.domain.Aeropuerto;
import com.zephyr.repository.AeropuertoRepository;
import com.zephyr.repository.impl.AeropuertoRepositoryJDBCImpl;
import com.zephyr.service.AeropuertoService;

import java.util.List;
import java.util.Optional;

public class AeropuertoServiceImpl implements AeropuertoService {
    private final AeropuertoRepository aeropuertoRepository;

    public AeropuertoServiceImpl() {
        this.aeropuertoRepository = new AeropuertoRepositoryJDBCImpl();
    }

    @Override
    public void registrarAeropuerto(Aeropuerto aeropuerto) {
        if (aeropuerto.getCodigoIata().length() != 3) {
            System.err.println("Advertencia: El código IATA debería tener 3 letras.");
        }
        aeropuertoRepository.save(aeropuerto);
    }

    @Override
    public List<Aeropuerto> listarTodos() {
        return aeropuertoRepository.findAll();
    }

    @Override
    public Optional<Aeropuerto> buscarPorId(int id) {
        return aeropuertoRepository.findById(id);
    }

    @Override
    public void actualizarAeropuerto(Aeropuerto aeropuerto) {
        aeropuertoRepository.update(aeropuerto);
    }

    @Override
    public void eliminarAeropuerto(int id) {
        aeropuertoRepository.delete(id);
    }
}

