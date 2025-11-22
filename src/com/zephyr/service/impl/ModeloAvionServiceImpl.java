package com.zephyr.service.impl;

import com.zephyr.domain.ModeloAvion;
import com.zephyr.repository.ModeloAvionRepository;
import com.zephyr.repository.impl.ModeloAvionRepositoryJDBCImpl;
import com.zephyr.service.ModeloAvionService;

import java.util.List;

public class ModeloAvionServiceImpl implements ModeloAvionService {

    private final ModeloAvionRepository modeloAvionRepository;

    public ModeloAvionServiceImpl(){
        this.modeloAvionRepository = new ModeloAvionRepositoryJDBCImpl();
    }

    @Override
    public List<ModeloAvion> listarModelos() {
        return modeloAvionRepository.findAll();
    }
}
