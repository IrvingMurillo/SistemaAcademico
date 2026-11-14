package com.sistema.modelo;

/** Datos propios del estudiante. El carnet es el identificador de su Usuario. */
public class Estudiante {
    private String carnet;
    private String carrera;

    public Estudiante(String carnet, String carrera) {
        this.carnet = carnet;
        this.carrera = carrera;
    }

    public String getCarnet() { return carnet; }
    public void setCarnet(String carnet) { this.carnet = carnet; }

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }
}