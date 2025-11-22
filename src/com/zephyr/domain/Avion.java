package com.zephyr.domain;

public class Avion {
    private int idAvion;
    private String matricula;
    private int anioFabricacion;
    private String estadoOperativo;
    private int idModeloAvion;
    private int idAerolinea;

    public Avion() {
    }

    public Avion(int idAvion, String matricula, int anioFabricacion, String estadoOperativo, int idModeloAvion, int idAerolinea) {
        this.idAvion = idAvion;
        this.matricula = matricula;
        this.anioFabricacion = anioFabricacion;
        this.estadoOperativo = estadoOperativo;
        this.idModeloAvion = idModeloAvion;
        this.idAerolinea = idAerolinea;
    }

    public int getIdAvion() {
        return idAvion;
    }

    public void setIdAvion(int idAvion) {
        this.idAvion = idAvion;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public int getAnioFabricacion() {
        return anioFabricacion;
    }

    public void setAnioFabricacion(int anioFabricacion) {
        this.anioFabricacion = anioFabricacion;
    }

    public String getEstadoOperativo() {
        return estadoOperativo;
    }

    public void setEstadoOperativo(String estadoOperativo) {
        this.estadoOperativo = estadoOperativo;
    }

    public int getIdModeloAvion() {
        return idModeloAvion;
    }

    public void setIdModeloAvion(int idModeloAvion) {
        this.idModeloAvion = idModeloAvion;
    }

    public int getIdAerolinea() {
        return idAerolinea;
    }

    public void setIdAerolinea(int idAerolinea) {
        this.idAerolinea = idAerolinea;
    }

    @Override
    public String toString() {
        return matricula;
    }
}
