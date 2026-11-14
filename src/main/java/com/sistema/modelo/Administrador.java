package com.sistema.modelo;

/** Datos propios del administrador. La credencial es el identificador de su Usuario. */
public class Administrador {
    private String credencial;

    public Administrador(String credencial) {
        this.credencial = credencial;
    }

    public String getCredencial() { return credencial; }
    public void setCredencial(String credencial) { this.credencial = credencial; }
}