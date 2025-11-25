package com.zephyr.repository;

import com.zephyr.domain.Vuelo;
import com.zephyr.domain.VueloRegistro;

import java.util.List;
import java.util.Optional;

public interface VueloRepository {
    List<Vuelo> findVuelosDetalladosDelDia();
    Optional<Vuelo> findVueloDetalladoById(int idVuelo);
    // crud luego
    //actualizar esto llama al SP
    void actualizarEstadoVuelo(int idVuelo, int idNuevoEstado);

    // para seccion Gestionar Vuelo
    void registrarVuelo(VueloRegistro vueloRegistro);

    //listar todos los vuelos
    List<Vuelo> findAll();
}
