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
import javax.swing.table.DefaultTableModel;

/**
 * Ventana base de cada rol: saludo y cierre de sesión abajo.
 * El centro y la parte de arriba quedan libres para el contenido de cada rol.
 */
public abstract class BasePOVForm extends BaseForm {

    protected BasePOVForm(Usuario usuario, String titulo) {
        super(titulo);

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

    /** Modelo de tabla de solo lectura, compartido por todos los POV. */
    protected static DefaultTableModel crearModelo(String... columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
    }
}