package com.zephyr.service.impl;

import com.zephyr.domain.Equipaje;
import com.zephyr.repository.EquipajeRepository;
import com.zephyr.repository.impl.EquipajeRepositoryJDBCImpl;
import com.zephyr.service.EquipajeService;

import java.util.List;
import java.util.UUID;

public class EquipajeServiceImpl implements EquipajeService {

    private final EquipajeRepository equipajeRepository;

    public EquipajeServiceImpl() {
        this.equipajeRepository = new EquipajeRepositoryJDBCImpl();
    }

    @Override
    public void registrarEquipaje(Equipaje equipaje) {
        if (equipaje.getCodigoEquipaje() == null || equipaje.getCodigoEquipaje().isEmpty()) {
            String codigoUnico = "BAG-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
            equipaje.setCodigoEquipaje(codigoUnico);
        }
        equipajeRepository.save(equipaje);
    }

    @Override
    public List<Equipaje> listarEquipajePorBoleto(int idBoleto) {
        return equipajeRepository.findByBoletoId(idBoleto);
    }
}
