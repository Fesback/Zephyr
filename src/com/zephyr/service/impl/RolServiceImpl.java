package com.zephyr.service.impl;

import com.zephyr.domain.Rol;
import com.zephyr.repository.RolRepository;
import com.zephyr.service.RolService;

import java.util.List;

public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    public RolServiceImpl(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public List<Rol> getRoles() {
        return rolRepository.findAll();
    }
}
