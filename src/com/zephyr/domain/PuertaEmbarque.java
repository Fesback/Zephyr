package com.zephyr.domain;

public class PuertaEmbarque {
    private int idPuertaEmbarque;
    private String codigoPuerta;
    private String ubicacionTerminal;
    private int idAeropuerto;

    public PuertaEmbarque() {

    }

    //constructor

    public PuertaEmbarque(int idPuertaEmbarque, String codigoPuerta, String ubicacionTerminal, int idAeropuerto) {
        this.idPuertaEmbarque = idPuertaEmbarque;
        this.codigoPuerta = codigoPuerta;
        this.ubicacionTerminal = ubicacionTerminal;
        this.idAeropuerto = idAeropuerto;
    }

    //getters and setters

    public int getIdPuertaEmbarque() {
        return idPuertaEmbarque;
    }

    public void setIdPuertaEmbarque(int idPuertaEmbarque) {
        this.idPuertaEmbarque = idPuertaEmbarque;
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

    public int getIdAeropuerto() {
        return idAeropuerto;
    }

    public void setIdAeropuerto(int idAeropuerto) {
        this.idAeropuerto = idAeropuerto;
    }

    //to string para el combobox
    @Override
    public String toString() {
        return codigoPuerta + " (" + ubicacionTerminal + ")";
    }
}
