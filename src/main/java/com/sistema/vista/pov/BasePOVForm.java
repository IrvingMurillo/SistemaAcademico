package com.sistema.vista.pov;

import com.sistema.modelo.Usuario;
import com.sistema.vista.BaseForm;
import com.sistema.vista.LoginForm;
import com.sistema.vista.Navegador;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Ventana base de cada rol: saludo y cierre de sesión abajo.
 * El centro queda libre para el contenido de cada rol.
 */
public abstract class BasePOVForm extends BaseForm {
    private static final int ANCHO_POV = 900;
    private static final int ALTO_POV = 650;

    protected BasePOVForm(Usuario usuario, String titulo) {
        super(titulo, ANCHO_POV, ALTO_POV);

        JLabel bienvenida = new JLabel("Bienvenido, " + usuario.getNombreCompleto()
                + " (" + usuario.getRol() + ")");

        JButton btnCerrarSesion = new JButton("Cerrar sesión");
        btnCerrarSesion.addActionListener(e -> Navegador.irA(new LoginForm()));

        JPanel abajo = new JPanel(new BorderLayout());
        abajo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        abajo.add(bienvenida, BorderLayout.WEST);
        abajo.add(btnCerrarSesion, BorderLayout.EAST);

        contenido.add(abajo, BorderLayout.SOUTH);
    }
}