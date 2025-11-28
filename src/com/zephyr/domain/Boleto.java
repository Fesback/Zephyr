package com.zephyr.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Boleto {

    private int idBoleto;
    private String codigoBoleto;
    private LocalDateTime fechaEEmision;
    private String asiento;
    private BigDecimal precioTotal;
    private int idPasajero;
    private int idVuelo;
    private int idClaseVuelo;
    private int idEstadoEmbarque;



    public Boleto() {
    }

    //Cosntructor

    public Boleto(int idBoleto, String codigoBoleto, LocalDateTime fechaEEmision, String asiento, BigDecimal precioTotal, int idPasajero, int idVuelo, int idClaseVuelo, int idEstadoEmbarque) {
        this.idBoleto = idBoleto;
        this.codigoBoleto = codigoBoleto;
        this.fechaEEmision = fechaEEmision;
        this.asiento = asiento;
        this.precioTotal = precioTotal;
        this.idPasajero = idPasajero;
        this.idVuelo = idVuelo;
        this.idClaseVuelo = idClaseVuelo;
        this.idEstadoEmbarque = idEstadoEmbarque;
    }

    // getters and setters

    public int getIdBoleto() {
        return idBoleto;
    }

    public void setIdBoleto(int idBoleto) {
        this.idBoleto = idBoleto;
    }

    public String getCodigoBoleto() {
        return codigoBoleto;
    }

    public void setCodigoBoleto(String codigoBoleto) {
        this.codigoBoleto = codigoBoleto;
    }

    public LocalDateTime getFechaEEmision() {
        return fechaEEmision;
    }

    public void setFechaEEmision(LocalDateTime fechaEEmision) {
        this.fechaEEmision = fechaEEmision;
    }

    public String getAsiento() {
        return asiento;
    }

    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public int getIdPasajero() {
        return idPasajero;
    }

    public void setIdPasajero(int idPasajero) {
        this.idPasajero = idPasajero;
    }

    public int getIdVuelo() {
        return idVuelo;
    }

    public void setIdVuelo(int idVuelo) {
        this.idVuelo = idVuelo;
    }

    public int getIdClaseVuelo() {
        return idClaseVuelo;
    }

    public void setIdClaseVuelo(int idClaseVuelo) {
        this.idClaseVuelo = idClaseVuelo;
    }

    public int getIdEstadoEmbarque() {
        return idEstadoEmbarque;
    }

    public void setIdEstadoEmbarque(int idEstadoEmbarque) {
        this.idEstadoEmbarque = idEstadoEmbarque;
    }
}
