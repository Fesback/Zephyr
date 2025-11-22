package com.zephyr.domain;

public class ModeloAvion {
    private int idModeloAvion;
    private String nombreModelo;
    private int capacidadPasajeros;
    private int autonomiaKm;

    public ModeloAvion() {
    }

    public ModeloAvion(int idModeloAvion, String nombreModelo, int capacidadPasajeros, int autonomiaKm) {
        this.idModeloAvion = idModeloAvion;
        this.nombreModelo = nombreModelo;
        this.capacidadPasajeros = capacidadPasajeros;
        this.autonomiaKm = autonomiaKm;
    }

    public int getIdModeloAvion() {
        return idModeloAvion;
    }

    public void setIdModeloAvion(int idModeloAvion) {
        this.idModeloAvion = idModeloAvion;
    }

    public String getNombreModelo() {
        return nombreModelo;
    }

    public void setNombreModelo(String nombreModelo) {
        this.nombreModelo = nombreModelo;
    }

    public int getCapacidadPasajeros() {
        return capacidadPasajeros;
    }

    public void setCapacidadPasajeros(int capacidadPasajeros) {
        this.capacidadPasajeros = capacidadPasajeros;
    }

    public int getAutonomiaKm() {
        return autonomiaKm;
    }

    public void setAutonomiaKm(int autonomiaKm) {
        this.autonomiaKm = autonomiaKm;
    }

    @Override
    public String toString() {
        return nombreModelo + " (" + capacidadPasajeros + " pax)";
    }
}
