package com.zephyr.service.impl;

import com.zephyr.domain.AsignacionPersonal;
import com.zephyr.repository.AsignacionRepository;
import com.zephyr.repository.impl.AsignacionRepositoryJDBCImpl;
import com.zephyr.service.AsignacionService;

import java.util.List;

public class AsignacionServiceImpl implements AsignacionService {

    private final AsignacionRepository repository;

    public AsignacionServiceImpl() {
        this.repository = new AsignacionRepositoryJDBCImpl();
    }

    @Override
    public void asignarPersonal(AsignacionPersonal asignacion) {
        repository.asignarPersonal(asignacion);
    }

    @Override
    public List<AsignacionPersonal> listarAsignacionesDeVuelo(int idVuelo) {
        return repository.listarPorVuelo(idVuelo);
    }
}
