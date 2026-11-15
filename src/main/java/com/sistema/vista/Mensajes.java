package com.sistema.vista;

import java.awt.Component;
import javax.swing.JOptionPane;

/** Diálogos y manejo de errores uniformes para todas las ventanas. */
public final class Mensajes {

    private Mensajes() {
    }

    public static void info(Component padre, String texto) {
        JOptionPane.showMessageDialog(padre, texto, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void aviso(Component padre, String texto) {
        JOptionPane.showMessageDialog(padre, texto, "Revise los datos", JOptionPane.WARNING_MESSAGE);
    }

    public static void error(Component padre, String texto) {
        JOptionPane.showMessageDialog(padre, texto, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /** Pregunta Sí/No y devuelve true si el usuario confirma. */
    public static boolean confirmar(Component padre, String pregunta) {
        return JOptionPane.showConfirmDialog(padre, pregunta, "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    /**
     * Ejecuta una acción y muestra el error si falla: un aviso con el motivo si los datos son
     * inválidos (IllegalArgumentException) o un error de conexión si falla MongoDB.
     *
     * @return true si la acción terminó sin errores
     */
    public static boolean ejecutar(Component padre, Runnable accion) {
        try {
            accion.run();
            return true;
        } catch (IllegalArgumentException ex) {
            aviso(padre, ex.getMessage());
        } catch (RuntimeException ex) {
            error(padre, "No se pudo completar la operación con la base de datos. "
                    + "Revise su conexión e intente de nuevo.");
        }
        return false;
    }
}