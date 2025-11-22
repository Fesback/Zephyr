package com.zephyr.service.impl;

import com.zephyr.domain.Avion;
import com.zephyr.repository.AvionRepository;
import com.zephyr.repository.impl.AvionRepositoryJDBCImpl;
import com.zephyr.service.AvionService;

import java.util.List;

public class AvionServiceImpl implements AvionService {

    private final AvionRepository avionRepository;

    public AvionServiceImpl() {
        this.avionRepository = new AvionRepositoryJDBCImpl();
    }

    @Override
    public void registrarAvion(Avion avion) {
        avionRepository.save(avion);
    }

    @Override
    public List<Avion> listarTodos() {
        return avionRepository.findAll();
    }

    @Override
    public void actualizarAvion(Avion avion) {
        avionRepository.update(avion);
    }

    @Override
    public void eliminarAvion(int id) {
        avionRepository.delete(id);
    }
}
