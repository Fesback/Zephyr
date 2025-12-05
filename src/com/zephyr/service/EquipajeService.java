package com.zephyr.service;

import com.zephyr.domain.Equipaje;

import java.util.List;

public interface EquipajeService {
    void registrarEquipaje(Equipaje equipaje);
    List<Equipaje> listarEquipajePorBoleto(int idBoleto);
}
