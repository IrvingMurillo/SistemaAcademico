package com.sistema.modelo;

/** Datos propios del profesor. El escalafón es el identificador de su Usuario. */
public class Profesor {
    private String escalafon;
    private String especializacion;

    public Profesor(String escalafon, String especializacion) {
        this.escalafon = escalafon;
        this.especializacion = especializacion;
    }

    public String getEscalafon() { return escalafon; }
    public void setEscalafon(String escalafon) { this.escalafon = escalafon; }

    public String getEspecializacion() { return especializacion; }
    public void setEspecializacion(String especializacion) { this.especializacion = especializacion; }
}