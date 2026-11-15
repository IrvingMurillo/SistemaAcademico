package com.sistema.modelo;

/** Materia del plan de estudios. La validez son las unidades valorativas (U.V.) que vale. */
public class Asignatura {
    private String sigla;
    private String nombre;
    private int validez;

    public Asignatura(String sigla, String nombre, int validez) {
        this.sigla = sigla;
        this.nombre = nombre;
        this.validez = validez;
    }

    public String getSigla() { return sigla; }
    public void setSigla(String sigla) { this.sigla = sigla; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getValidez() { return validez; }
    public void setValidez(int validez) { this.validez = validez; }

    /** Texto para combos y tablas: "sigla - nombre". */
    @Override
    public String toString() {
        return sigla + " - " + nombre;
    }
}