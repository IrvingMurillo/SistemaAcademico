package com.sistema.controlador;

import java.util.regex.Pattern;

/** Validaciones de datos. Cada método lanza IllegalArgumentException con un mensaje claro. */
public class ValidadorDatos {
    private static final Pattern CORREO = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    /** El valor no puede ser nulo, vacío ni solo espacios. */
    public static void requerido(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
        }
    }

    /** El correo es obligatorio y debe tener formato nombre@dominio.com. */
    public static void correo(String correo) {
        requerido(correo, "correo");
        if (!CORREO.matcher(correo.trim()).matches()) {
            throw new IllegalArgumentException("El correo no tiene un formato válido (ejemplo: nombre@dominio.com).");
        }
    }

    /** La contraseña es obligatoria y debe tener al menos 6 caracteres. */
    public static void password(String password) {
        requerido(password, "contraseña");
        if (password.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        }
    }

    /** Convierte el texto a un entero mayor que cero o lanza un error que explica el problema. */
    public static int enteroPositivo(String texto, String campo) {
        requerido(texto, campo);
        try {
            int valor = Integer.parseInt(texto.trim());
            if (valor <= 0) {
                throw new IllegalArgumentException("El campo " + campo + " debe ser mayor que cero.");
            }
            return valor;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo " + campo + " debe ser un número entero (ejemplo: 4).");
        }
    }
}