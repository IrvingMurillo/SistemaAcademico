package com.sistema.vista;

import com.sistema.controlador.AutenticacionControlador;
import com.sistema.modelo.Usuario;
import com.sistema.vista.pov.AdministradorPOVForm;
import com.sistema.vista.pov.EstudiantePOVForm;
import com.sistema.vista.pov.ProfesorPOVForm;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginForm extends JFrame {
    private final JTextField txtCorreo = new JTextField(20);
    private final JPasswordField txtPassword = new JPasswordField(20);
    private final JButton btnRegistrarse = new JButton("Registrarse");
    private final JButton btnIngresar = new JButton("Ingresar");
    private final AutenticacionControlador controlador = new AutenticacionControlador();

    public LoginForm() {
        super("Sistema Académico - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel titulo = new JLabel("LOGIN", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 22f));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JPanel campos = new JPanel(new GridLayout(2, 2, 10, 15));
        campos.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        campos.add(new JLabel("Correo:"));
        campos.add(txtCorreo);
        campos.add(new JLabel("Contraseña:"));
        campos.add(txtPassword);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnRegistrarse);
        botones.add(btnIngresar);

        add(titulo, BorderLayout.NORTH);
        add(campos, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);

        btnIngresar.addActionListener(e -> ingresar());
        btnRegistrarse.addActionListener(e -> abrirRegistro());
        getRootPane().setDefaultButton(btnIngresar); // Enter = Ingresar

        pack();
        setLocationRelativeTo(null);
    }

    private void ingresar() {
        try {
            Usuario usuario = controlador.iniciarSesion(
                    txtCorreo.getText(), new String(txtPassword.getPassword()));
            abrirPov(usuario);
        } catch (IllegalArgumentException ex) {
            txtPassword.setText("");
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "No se pudo iniciar sesión", JOptionPane.WARNING_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo conectar con la base de datos. Intente de nuevo.",
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Abre la ventana que corresponde al rol del usuario. */
    private void abrirPov(Usuario usuario) {
        JFrame ventana = switch (usuario.getRol()) {
            case ESTUDIANTE -> new EstudiantePOVForm(usuario);
            case PROFESOR -> new ProfesorPOVForm(usuario);
            case ADMINISTRADOR -> new AdministradorPOVForm(usuario);
        };
        ventana.setVisible(true);
        dispose();
    }

    private void abrirRegistro() {
        new RegistroForm().setVisible(true);
        dispose();
    }
}