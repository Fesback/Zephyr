package com.zephyr.domain;

public class Aerolinea {

    private int idAerolinea;
    private String nombre;
    private String codigoIata;
    private String paisOrigen;
    private String telefonoContacto;

    //Constructor vacio
    public Aerolinea() {
    }

    //Constructor
    public Aerolinea(int idAerolinea, String nombre, String codigoIata, String paisOrigen, String telefonoContacto) {
        this.idAerolinea = idAerolinea;
        this.nombre = nombre;
        this.codigoIata = codigoIata;
        this.paisOrigen = paisOrigen;
        this.telefonoContacto = telefonoContacto;
    }

    //getters and setters
    public int getIdAerolinea() {
        return idAerolinea;
    }

    public void setIdAerolinea(int idAerolinea) {
        this.idAerolinea = idAerolinea;
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

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }
}
