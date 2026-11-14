package com.sistema.controlador;

import java.util.regex.Pattern;

/** Cada método lanza IllegalArgumentException con un mensaje claro si el dato es inválido. */
public class ValidadorDatos {
    private static final Pattern CORREO = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    public static void requerido(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
        }
    }

    public static void correo(String correo) {
        if (!CORREO.matcher(correo.trim()).matches()) {
            throw new IllegalArgumentException("El correo no tiene un formato válido (ejemplo: nombre@dominio.com).");
        }
    }

    public static void password(String password) {
        if (password.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        }
    }
}