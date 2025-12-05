package com.zephyr.domain;

import java.time.LocalDate;

public class AsignacionPersonal {
    private int idAsignacion;
    private String rolEnAsignacion;
    private LocalDate fechaAsignacion;
    private int idPersonal;
    private int idVuelo;

    public AsignacionPersonal() {

    }

    public AsignacionPersonal(int idAsignacion, String rolEnAsignacion, LocalDate fechaAsignacion, int idPersonal, int idVuelo) {
        this.idAsignacion = idAsignacion;
        this.rolEnAsignacion = rolEnAsignacion;
        this.fechaAsignacion = fechaAsignacion;
        this.idPersonal = idPersonal;
        this.idVuelo = idVuelo;
    }

    public int getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(int idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    public String getRolEnAsignacion() {
        return rolEnAsignacion;
    }

    public void setRolEnAsignacion(String rolEnAsignacion) {
        this.rolEnAsignacion = rolEnAsignacion;
    }

    public LocalDate getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDate fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public int getIdPersonal() {
        return idPersonal;
    }

    public void setIdPersonal(int idPersonal) {
        this.idPersonal = idPersonal;
    }

    public int getIdVuelo() {
        return idVuelo;
    }

    public void setIdVuelo(int idVuelo) {
        this.idVuelo = idVuelo;
    }
}
