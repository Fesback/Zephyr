package com.zephyr.domain;

public class Aeropuerto {
    private int idAeropuerto;
    private String nombre;
    private String codigoIata;
    private String direccion;
    private int idCiudad;

    //constructor vacio
    public Aeropuerto() {
    }

    //constructor completo
    public Aeropuerto(int idAeropuerto, String nombre, String codigoIata, String direccion, int idCiudad) {
        this.idAeropuerto = idAeropuerto;
        this.nombre = nombre;
        this.codigoIata = codigoIata;
        this.direccion = direccion;
        this.idCiudad = idCiudad;
    }

    //getters and setters

    public int getIdAeropuerto() {
        return idAeropuerto;
    }

    public void setIdAeropuerto(int idAeropuerto) {
        this.idAeropuerto = idAeropuerto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoIata() {
        return codigoIata;
    }

    public void setCodigoIata(String codigoIata) {
        this.codigoIata = codigoIata;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(int idCiudad) {
        this.idCiudad = idCiudad;
    }

    //mostrar en UI
    @Override
    public String toString() {
        return codigoIata + " - " + nombre;
    }
}
