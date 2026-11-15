package com.sistema.modelo;

public class Curso {
    private String nrc;
    private String asignatura;
    private String profesor;
    private int cupos;

    public Curso() {
    }

    public Curso(String nrc, String asignatura, String profesor, int cupos) {
        this.nrc = nrc;
        this.asignatura = asignatura;
        this.profesor = profesor;
        this.cupos = cupos;
    }

    public String getNrc() {
        return nrc;
    }

    public void setNrc(String nrc) {
        this.nrc = nrc;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public int getCupos() {
        return cupos;
    }

    public void setCupos(int cupos) {
        this.cupos = cupos;
    }

    @Override
    public String toString() {
        return nrc + " - " + asignatura;
    }
}
