package com.sistema.modelo;

public class Asignatura {
    private String sigla;
    private String nombre;
    private String validez;

    public Asignatura() {
    }

    public Asignatura(String sigla, String nombre, String validez) {
        this.sigla = sigla;
        this.nombre = nombre;
        this.validez = validez;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getValidez() {
        return validez;
    }

    public void setValidez(String validez) {
        this.validez = validez;
    }

    @Override
    public String toString() {
        return sigla + " - " + nombre;
    }
}
