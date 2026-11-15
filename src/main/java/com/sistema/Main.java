package com.sistema;

import com.sistema.persistencia.ConexionMongoDB;
import com.sistema.vista.LoginForm;
import com.sistema.vista.Navegador;
import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Punto de entrada: verifica la conexión con MongoDB y abre el login. */
public class Main {

    public static void main(String[] args) {
        UIManager.put("Panel.background", Color.WHITE); // fondo blanco en todas las ventanas

        try {
            ConexionMongoDB.prepararBaseDatos(); // además comprueba la conexión
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(null,
                    "No se pudo conectar con MongoDB.\nRevise config.properties y su conexión a internet.",
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        SwingUtilities.invokeLater(() -> Navegador.irA(new LoginForm()));
    }
}