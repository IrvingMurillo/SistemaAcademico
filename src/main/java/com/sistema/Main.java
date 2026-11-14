package com.sistema;

import com.sistema.persistencia.UsuarioDAO;
import com.sistema.vista.LoginForm;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        try {
            new UsuarioDAO().crearIndices(); // además comprueba la conexión
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(null,
                    "No se pudo conectar con MongoDB.\nRevise config.properties y su conexión a internet.",
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}