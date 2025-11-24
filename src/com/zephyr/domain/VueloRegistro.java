package com.zephyr.domain;

import java.time.LocalDateTime;

public class VueloRegistro {
    private String codigoVuelo;
    private LocalDateTime fechaSalida;
    private LocalDateTime fechaLlegada;
    private int idAerolinea;
    private int idAvion;
    private int idAeropuertoOrigen;
    private int idAeropuertoDestino;
    private int idEstadoVuelo;

    public VueloRegistro() {

    }

    //Constructor comp,eto

    public VueloRegistro(String codigoVuelo, LocalDateTime fechaSalida, LocalDateTime fechaLlegada, int idAerolinea, int idAvion, int idAeropuertoOrigen, int idAeropuertoDestino, int idEstadoVuelo) {
        this.codigoVuelo = codigoVuelo;
        this.fechaSalida = fechaSalida;
        this.fechaLlegada = fechaLlegada;
        this.idAerolinea = idAerolinea;
        this.idAvion = idAvion;
        this.idAeropuertoOrigen = idAeropuertoOrigen;
        this.idAeropuertoDestino = idAeropuertoDestino;
        this.idEstadoVuelo = idEstadoVuelo;
    }

    //getters and setters

    public String getCodigoVuelo() {
        return codigoVuelo;
    }

    public void setCodigoVuelo(String codigoVuelo) {
        this.codigoVuelo = codigoVuelo;
    }

    public LocalDateTime getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDateTime fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public LocalDateTime getFechaLlegada() {
        return fechaLlegada;
    }

    public void setFechaLlegada(LocalDateTime fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }

    public int getIdAerolinea() {
        return idAerolinea;
    }

    public void setIdAerolinea(int idAerolinea) {
        this.idAerolinea = idAerolinea;
    }

    public int getIdAvion() {
        return idAvion;
    }

    public void setIdAvion(int idAvion) {
        this.idAvion = idAvion;
    }

    public int getIdAeropuertoOrigen() {
        return idAeropuertoOrigen;
    }

    public void setIdAeropuertoOrigen(int idAeropuertoOrigen) {
        this.idAeropuertoOrigen = idAeropuertoOrigen;
    }

    public int getIdAeropuertoDestino() {
        return idAeropuertoDestino;
    }

    public void setIdAeropuertoDestino(int idAeropuertoDestino) {
        this.idAeropuertoDestino = idAeropuertoDestino;
    }

    public int getIdEstadoVuelo() {
        return idEstadoVuelo;
    }

    public void setIdEstadoVuelo(int idEstadoVuelo) {
        this.idEstadoVuelo = idEstadoVuelo;
    }
}
