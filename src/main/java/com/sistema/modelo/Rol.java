package com.sistema.modelo;

/** Roles del sistema. Cada rol identifica a sus usuarios con un código distinto. */
public enum Rol {
    ESTUDIANTE("carnet"),
    PROFESOR("escalafón"),
    ADMINISTRADOR("credencial");

    private final String nombreCodigo; // cómo se llama su código de identificación

    Rol(String nombreCodigo) {
        this.nombreCodigo = nombreCodigo;
    }

    /** Devuelve cómo se llama el código del rol (carnet, escalafón o credencial). */
    public String getNombreCodigo() {
        return nombreCodigo;
    }
}