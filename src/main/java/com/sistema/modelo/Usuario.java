package com.sistema.modelo;

/** Datos de acceso comunes a todos los roles. */
public class Usuario {
    private String identificador; // carnet, escalafón o credencial
    private String nombreCompleto;
    private String correo;
    private String passwordHash;
    private Rol rol;

    public Usuario(String identificador, String nombreCompleto, String correo,
                   String passwordHash, Rol rol) {
        this.identificador = identificador;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.passwordHash = passwordHash;
        this.rol = rol;
    }

    public String getIdentificador() { return identificador; }
    public void setIdentificador(String identificador) { this.identificador = identificador; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    @Override
    public String toString() { // nunca incluye la contraseña
        return nombreCompleto + " (" + rol + ")";
    }
}