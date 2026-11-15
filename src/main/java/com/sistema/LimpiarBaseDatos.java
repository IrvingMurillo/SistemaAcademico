package com.sistema;

import com.sistema.persistencia.ConexionMongoDB;
import javax.swing.JOptionPane;

/** Borra TODA la base de datos de pruebas. Pide confirmación antes de hacerlo. */
public class LimpiarBaseDatos {

    public static void main(String[] args) {
        int respuesta = JOptionPane.showConfirmDialog(null,
                "Se borrarán TODOS los datos: usuarios, asignaturas, cursos y matrículas.\n"
                + "¿Desea continuar?",
                "Limpiar base de datos", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (respuesta == JOptionPane.YES_OPTION) {
            limpiar();
        } else {
            System.out.println("Operación cancelada. No se borró nada.");
        }
        System.exit(0);
    }

    private static void limpiar() {
        System.out.println("=== INICIANDO LIMPIEZA TOTAL DE LA BASE DE DATOS ===");
        try {
            ConexionMongoDB.getDatabase().drop();
            ConexionMongoDB.prepararBaseDatos(); // el drop también borra los índices únicos

            System.out.println("[+] Base de datos limpiada. No queda ningún usuario.");
            System.out.println("[+] Ejecute ahora CrearRegistros para cargar los datos de prueba.");
        } catch (RuntimeException e) {
            System.out.println("[-] Error al intentar limpiar la base de datos: " + e.getMessage());
        }
    }
}