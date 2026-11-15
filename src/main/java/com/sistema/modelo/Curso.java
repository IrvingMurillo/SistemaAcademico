package com.sistema.modelo;

/** Oferta de una asignatura con un profesor y un cupo máximo. */
public class Curso {
    private String nrc;        // código único del curso
    private String asignatura; // sigla de la asignatura
    private String profesor;   // escalafón del profesor
    private int cupos;         // cupo máximo

    public Curso(String nrc, String asignatura, String profesor, int cupos) {
        this.nrc = nrc;
        this.asignatura = asignatura;
        this.profesor = profesor;
        this.cupos = cupos;
    }

    public String getNrc() { return nrc; }
    public void setNrc(String nrc) { this.nrc = nrc; }

    public String getAsignatura() { return asignatura; }
    public void setAsignatura(String asignatura) { this.asignatura = asignatura; }

    public String getProfesor() { return profesor; }
    public void setProfesor(String profesor) { this.profesor = profesor; }

    public int getCupos() { return cupos; }
    public void setCupos(int cupos) { this.cupos = cupos; }

    /** Texto para combos y tablas: "nrc - asignatura". */
    @Override
    public String toString() {
        return nrc + " - " + asignatura;
    }
}