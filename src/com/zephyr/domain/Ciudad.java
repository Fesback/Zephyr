package com.zephyr.domain;

public class Ciudad {
    private int idCiudad;
    private String nombre;
    private String codigoCiudad;
    private int idPais;

    public Ciudad() {
    }

    public Ciudad(int idCiudad, String nombre, String codigoCiudad, int idPais) {
        this.idCiudad = idCiudad;
        this.nombre = nombre;
        this.codigoCiudad = codigoCiudad;
        this.idPais = idPais;
    }

    // Getters y Setters
    public int getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(int idCiudad) {
        this.idCiudad = idCiudad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoCiudad() {
        return codigoCiudad;
    }

    public void setCodigoCiudad(String codigoCiudad) {
        this.codigoCiudad = codigoCiudad;
    }

    public int getIdPais() {
        return idPais;
    }

    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
