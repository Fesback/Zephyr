package com.zephyr.service.impl;

import com.zephyr.domain.Ciudad;
import com.zephyr.repository.CiudadRepository;
import com.zephyr.repository.impl.CiudadRepositoryJDBCImpl;
import com.zephyr.service.CiudadService;

import java.util.List;

public class CiudadServiceImpl implements CiudadService {
    private final CiudadRepository ciudadRepository;

    public CiudadServiceImpl() {
        this.ciudadRepository = new CiudadRepositoryJDBCImpl();
    }

    @Override
    public List<Ciudad> listarTodas() {
        return ciudadRepository.findAll();
    }
}
