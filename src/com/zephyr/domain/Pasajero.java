package com.zephyr.domain;

import java.time.LocalDate;

public class Pasajero {

    private int idPasajero;
    private String nombres;
    private String apellidos;
    private LocalDate fechanNacimiento;
    private String numeroDocumento;
    private String nacionalidad;
    private String correo;
    private String telefono;
    private int idTipoDocumento;

    // constructor
    public Pasajero(int idPasajero, String nombres, String apellidos, LocalDate fechanNacimiento, String numeroDocumento, String nacionalidad, String correo, String telefono, int idTipoDocumento) {
        this.idPasajero = idPasajero;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechanNacimiento = fechanNacimiento;
        this.numeroDocumento = numeroDocumento;
        this.nacionalidad = nacionalidad;
        this.correo = correo;
        this.telefono = telefono;
        this.idTipoDocumento = idTipoDocumento;
    }

    // getters and setters

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

    public LocalDate getFechanNacimiento() {
        return fechanNacimiento;
    }

    public void setFechanNacimiento(LocalDate fechanNacimiento) {
        this.fechanNacimiento = fechanNacimiento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }
}
