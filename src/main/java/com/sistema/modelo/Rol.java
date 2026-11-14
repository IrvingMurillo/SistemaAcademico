package com.sistema.modelo;

public enum Rol {
    ESTUDIANTE("carnet"),
    PROFESOR("escalafón"),
    ADMINISTRADOR("credencial");

    private final String nombreCodigo; // cómo se llama su código de identificación

    Rol(String nombreCodigo) {
        this.nombreCodigo = nombreCodigo;
    }

    public String getNombreCodigo() {
        return nombreCodigo;
    }
}