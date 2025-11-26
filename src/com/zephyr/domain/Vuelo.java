package com.zephyr.domain;

import java.time.LocalDateTime;

public class Vuelo {
    private int idVuelo;
    private String codigoVuelo;
    private LocalDateTime fechaSalida;
    private LocalDateTime fechaLlegada;
    private String estadoVuelo;

    private int idAerolinea;
    private int idAvion;
    private int idAeropuertoOrigen;
    private int idAeropuertoDestino;
    private int idPuerta;

    private String aerolinea;
    private String aerolineaIata;
    private String avionMatricula;
    private String avionModelo;
    private int capacidadPasajeros;

    private String aeropuertoOrigen;
    private String origenIata;
    private String ciudadOrigen;

    private String aeropuertoDestino;
    private String destinoIata;
    private String ciudadDestino;

    private String codigoPuerta;
    private String ubicacionTerminal;

    public Vuelo() {
    }

    // --- Constructor
    public Vuelo(int idVuelo, String codigoVuelo, LocalDateTime fechaSalida, LocalDateTime fechaLlegada,
                 // IDs
                 int idAerolinea, int idAvion, int idAeropuertoOrigen, int idAeropuertoDestino, int idPuerta,
                 // Textos
                 String estadoVuelo, String aerolinea, String aerolineaIata, String avionMatricula, String avionModelo,
                 int capacidadPasajeros, String aeropuertoOrigen, String origenIata, String ciudadOrigen,
                 String aeropuertoDestino, String destinoIata, String ciudadDestino,
                 String codigoPuerta, String ubicacionTerminal) {

        this.idVuelo = idVuelo;
        this.codigoVuelo = codigoVuelo;
        this.fechaSalida = fechaSalida;
        this.fechaLlegada = fechaLlegada;

        this.idAerolinea = idAerolinea;
        this.idAvion = idAvion;
        this.idAeropuertoOrigen = idAeropuertoOrigen;
        this.idAeropuertoDestino = idAeropuertoDestino;
        this.idPuerta = idPuerta;

        this.estadoVuelo = estadoVuelo;
        this.aerolinea = aerolinea;
        this.aerolineaIata = aerolineaIata;
        this.avionMatricula = avionMatricula;
        this.avionModelo = avionModelo;
        this.capacidadPasajeros = capacidadPasajeros;
        this.aeropuertoOrigen = aeropuertoOrigen;
        this.origenIata = origenIata;
        this.ciudadOrigen = ciudadOrigen;
        this.aeropuertoDestino = aeropuertoDestino;
        this.destinoIata = destinoIata;
        this.ciudadDestino = ciudadDestino;
        this.codigoPuerta = codigoPuerta;
        this.ubicacionTerminal = ubicacionTerminal;
    }

    // Getters and setters

    public int getIdVuelo() {
        return idVuelo;
    }

    public void setIdVuelo(int idVuelo) {
        this.idVuelo = idVuelo;
    }

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

    public String getEstadoVuelo() {
        return estadoVuelo;
    }

    public void setEstadoVuelo(String estadoVuelo) {
        this.estadoVuelo = estadoVuelo;
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

    public int getIdPuerta() {
        return idPuerta;
    }

    public void setIdPuerta(int idPuerta) {
        this.idPuerta = idPuerta;
    }

    public String getAerolinea() {
        return aerolinea;
    }

    public void setAerolinea(String aerolinea) {
        this.aerolinea = aerolinea;
    }

    public String getAerolineaIata() {
        return aerolineaIata;
    }

    public void setAerolineaIata(String aerolineaIata) {
        this.aerolineaIata = aerolineaIata;
    }

    public String getAvionMatricula() {
        return avionMatricula;
    }

    public void setAvionMatricula(String avionMatricula) {
        this.avionMatricula = avionMatricula;
    }

    public String getAvionModelo() {
        return avionModelo;
    }

    public void setAvionModelo(String avionModelo) {
        this.avionModelo = avionModelo;
    }

    public int getCapacidadPasajeros() {
        return capacidadPasajeros;
    }

    public void setCapacidadPasajeros(int capacidadPasajeros) {
        this.capacidadPasajeros = capacidadPasajeros;
    }

    public String getAeropuertoOrigen() {
        return aeropuertoOrigen;
    }

    public void setAeropuertoOrigen(String aeropuertoOrigen) {
        this.aeropuertoOrigen = aeropuertoOrigen;
    }

    public String getOrigenIata() {
        return origenIata;
    }

    public void setOrigenIata(String origenIata) {
        this.origenIata = origenIata;
    }

    public String getCiudadOrigen() {
        return ciudadOrigen;
    }

    public void setCiudadOrigen(String ciudadOrigen) {
        this.ciudadOrigen = ciudadOrigen;
    }

    public String getAeropuertoDestino() {
        return aeropuertoDestino;
    }

    public void setAeropuertoDestino(String aeropuertoDestino) {
        this.aeropuertoDestino = aeropuertoDestino;
    }

    public String getDestinoIata() {
        return destinoIata;
    }

    public void setDestinoIata(String destinoIata) {
        this.destinoIata = destinoIata;
    }

    public String getCiudadDestino() {
        return ciudadDestino;
    }

    public void setCiudadDestino(String ciudadDestino) {
        this.ciudadDestino = ciudadDestino;
    }

    public String getCodigoPuerta() {
        return codigoPuerta;
    }

    public void setCodigoPuerta(String codigoPuerta) {
        this.codigoPuerta = codigoPuerta;
    }

    public String getUbicacionTerminal() {
        return ubicacionTerminal;
    }

    public void setUbicacionTerminal(String ubicacionTerminal) {
        this.ubicacionTerminal = ubicacionTerminal;
    }
}
