package com.zephyr.repository.impl;

import com.zephyr.domain.Personal;
import com.zephyr.domain.Rol;
import com.zephyr.repository.PersonalRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonalRepositoryImpl implements PersonalRepository {

    //TODO:Esta version es simulada,, no usara la base de datos dee postgre, usara un arraylist

    private List<Personal> baseDeDatosSimulada;
    private int idCounter = 0;

    public PersonalRepositoryImpl(){
        this.baseDeDatosSimulada = new ArrayList<>();

        Rol rolAdmin = new Rol(1, "Administrador", "Rol de admin");
        Rol rolSupervisor = new Rol(2, "Supervisro de Operaciones", "Rol de Supervisor");
        Rol rolAgente = new Rol(3, "Agente de Puerta", "Rol de agente");

        // Creare users de prueb
         save(new Personal(1, "Admin", "Zephyr", "admin@zephyr.com", "admin123", rolAdmin));
         save(new Personal(2, "Sara", "Connor", "s.connor@zephyr.com", "super123", rolSupervisor));
         save(new Personal(3, "Michael", "Scott", "m.scott@zephyr.com",  "agente123", rolAgente));
    }

    // Read
    @Override
    public Optional<Personal> findbyCorreo(String correo) {
        for (Personal personal : this.baseDeDatosSimulada) {
            if(personal.getCorreo().equalsIgnoreCase(correo)){
                return Optional.of(personal);
            }
        }
        return Optional.empty();
    }

    // create
    @Override
    public void save(Personal personal) {
        this.idCounter++;
        personal.setIdPersonal(this.idCounter);
        this.baseDeDatosSimulada.add(personal);
        System.out.println("[Debug Repo] Agente " + personal.getNombres() + " (ID=" + personal.getIdPersonal() + ") guardado.");
    }

    @Override
    public Optional<Personal> findbyId(int id) {
        for (Personal personal : this.baseDeDatosSimulada) {
            if(personal.getIdPersonal() == id){
                return Optional.of(personal);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Personal> findAll() {
        return new  ArrayList<>(this.baseDeDatosSimulada);
    }

    @Override
    public void update(Personal personalActualizado) {
        for (int i = 0; i < this.baseDeDatosSimulada.size(); i++) {
            Personal p = this.baseDeDatosSimulada.get(i);
            if(p.getIdPersonal() == personalActualizado.getIdPersonal()){
                this.baseDeDatosSimulada.set(i, personalActualizado);
                System.out.println("[Debug Repo] Agente ID=" + p.getIdPersonal() + " actualizado.");
                return;
            }
        }

    }

    @Override
    public void delete(int id) {
        boolean removed = this.baseDeDatosSimulada.removeIf(personal -> personal.getIdPersonal() == id);
        if (removed) {
            System.out.println("[Debug Repo] Agente ID=" + id + " eliminado.");
        } else {
            System.out.println("[Debug Repo] No se encontro Agente ID=" + id + " para eliminar.");
        }
    }
}
