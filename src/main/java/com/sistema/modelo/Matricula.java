package com.sistema.modelo;

/** Relación entre un estudiante y un curso. Retirarse cambia el estado, no borra el registro. */
public class Matricula {
    public static final String ACTIVA = "ACTIVA";
    public static final String RETIRADA = "RETIRADA";

    private String codigo;
    private String carnet;
    private String nrc;
    private String fecha; // formato aaaa-mm-dd
    private String estado;

    public Matricula(String codigo, String carnet, String nrc, String fecha, String estado) {
        this.codigo = codigo;
        this.carnet = carnet;
        this.nrc = nrc;
        this.fecha = fecha;
        this.estado = estado;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getCarnet() { return carnet; }
    public void setCarnet(String carnet) { this.carnet = carnet; }

    public String getNrc() { return nrc; }
    public void setNrc(String nrc) { this.nrc = nrc; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}