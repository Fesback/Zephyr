package com.zephyr.domain;

public class PasajeroPorVuelo {
    private int idVuelo;
    private int idPasajero;
    private String nombres;
    private String apellidos;
    private String numeroDocumento;
    private String tipoDocumento;
    private int idBoleto;
    private String codigoBoleto;
    private String asiento;
    private String clasesVuelo;
    private String estadoEmbarque;

    //Consturctor

    public PasajeroPorVuelo(int idVuelo, int idPasajero, String nombres, String apellidos, String numeroDocumento, String tipoDocumento, int idBoleto, String codigoBoleto, String asiento, String clasesVuelo, String estadoEmbarque) {
        this.idVuelo = idVuelo;
        this.idPasajero = idPasajero;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.numeroDocumento = numeroDocumento;
        this.tipoDocumento = tipoDocumento;
        this.idBoleto = idBoleto;
        this.codigoBoleto = codigoBoleto;
        this.asiento = asiento;
        this.clasesVuelo = clasesVuelo;
        this.estadoEmbarque = estadoEmbarque;
    }

    //getters and setter

    public int getIdVuelo() {
        return idVuelo;
    }

    public void setIdVuelo(int idVuelo) {
        this.idVuelo = idVuelo;
    }

    public int getIdPasajero() {
        return idPasajero;
    }

    public void setIdPasajero(int idPasajero) {
        this.idPasajero = idPasajero;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

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

    public String getAsiento() {
        return asiento;
    }

    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }

    public String getClasesVuelo() {
        return clasesVuelo;
    }

    public void setClasesVuelo(String clasesVuelo) {
        this.clasesVuelo = clasesVuelo;
    }

    public String getEstadoEmbarque() {
        return estadoEmbarque;
    }

    public void setEstadoEmbarque(String estadoEmbarque) {
        this.estadoEmbarque = estadoEmbarque;
    }
}
