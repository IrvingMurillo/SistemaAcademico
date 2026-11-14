package com.sistema.vista.pov;

import com.sistema.modelo.Usuario;
import com.sistema.vista.LoginForm;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/** Ventana base de cada rol: saludo y cierre de sesión. */
public abstract class BasePOVForm extends JFrame {

    protected BasePOVForm(Usuario usuario, String titulo) {
        super(titulo);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel bienvenida = new JLabel("Bienvenido, " + usuario.getNombreCompleto()
                + " (" + usuario.getRol() + ")", SwingConstants.CENTER);

        JButton btnCerrarSesion = new JButton("Cerrar sesión");
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        JPanel abajo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        abajo.add(btnCerrarSesion);

        add(bienvenida, BorderLayout.CENTER);
        add(abajo, BorderLayout.SOUTH);

        setSize(500, 350);
        setLocationRelativeTo(null);
    }

    private void cerrarSesion() {
        new LoginForm().setVisible(true);
        dispose();
    }
}