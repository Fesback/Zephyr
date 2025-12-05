package com.zephyr.domain;

public class Equipaje {

    private int idEquipaje;
    private String codigoEquipaje;
    private double peso;
    private String dimensiones;
    private String tipo;
    private int idBoleto;

    public Equipaje() {
    }

    public Equipaje(int idEquipaje, String codigoEquipaje, double peso, String dimensiones, String tipo, int idBoleto) {
        this.idEquipaje = idEquipaje;
        this.codigoEquipaje = codigoEquipaje;
        this.peso = peso;
        this.dimensiones = dimensiones;
        this.tipo = tipo;
        this.idBoleto = idBoleto;
    }

    public int getIdEquipaje() {
        return idEquipaje;
    }

    public void setIdEquipaje(int idEquipaje) {
        this.idEquipaje = idEquipaje;
    }

    public String getCodigoEquipaje() {
        return codigoEquipaje;
    }

    public void setCodigoEquipaje(String codigoEquipaje) {
        this.codigoEquipaje = codigoEquipaje;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getDimensiones() {
        return dimensiones;
    }

    public void setDimensiones(String dimensiones) {
        this.dimensiones = dimensiones;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getIdBoleto() {
        return idBoleto;
    }

    public void setIdBoleto(int idBoleto) {
        this.idBoleto = idBoleto;
    }
}
