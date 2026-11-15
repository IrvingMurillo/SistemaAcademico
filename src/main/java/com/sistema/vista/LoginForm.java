package com.sistema.vista;

import com.sistema.controlador.AutenticacionControlador;
import com.sistema.modelo.Usuario;
import com.sistema.vista.pov.AdministradorPOVForm;
import com.sistema.vista.pov.EstudiantePOVForm;
import com.sistema.vista.pov.ProfesorPOVForm;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginForm extends BaseForm {
    private final JTextField txtCorreo = new JTextField(20);
    private final JPasswordField txtPassword = new JPasswordField(20);
    private final AutenticacionControlador controlador = new AutenticacionControlador();

    public LoginForm() {
        super("Sistema Académico - Login");

        JLabel titulo = new JLabel("LOGIN", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 24f));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JPanel campos = new JPanel(new GridLayout(2, 2, 10, 15));
        campos.add(new JLabel("Correo:"));
        campos.add(txtCorreo);
        campos.add(new JLabel("Contraseña:"));
        campos.add(txtPassword);

        JPanel centro = new JPanel(new GridBagLayout()); // centra los campos con su tamaño natural
        centro.add(campos);

        JButton btnRegistrarse = new JButton("Registrarse");
        JButton btnIngresar = new JButton("Ingresar");
        btnRegistrarse.addActionListener(e -> Navegador.irA(new RegistroForm()));
        btnIngresar.addActionListener(e -> ingresar());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        botones.add(btnRegistrarse);
        botones.add(btnIngresar);

        contenido.add(titulo, BorderLayout.NORTH);
        contenido.add(centro, BorderLayout.CENTER);
        contenido.add(botones, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnIngresar); // Enter = Ingresar
    }

    private void ingresar() {
        try {
            Usuario usuario = controlador.iniciarSesion(
                    txtCorreo.getText(), new String(txtPassword.getPassword()));
            Navegador.irA(crearPov(usuario));
        } catch (IllegalArgumentException ex) { // datos inválidos o credenciales incorrectas
            txtPassword.setText("");
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Revise los datos", JOptionPane.WARNING_MESSAGE);
        } catch (RuntimeException ex) { // problema con MongoDB
            JOptionPane.showMessageDialog(this,
                    "No se pudo conectar con la base de datos. Intente de nuevo.",
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Devuelve la ventana que corresponde al rol del usuario. */
    private BaseForm crearPov(Usuario usuario) {
        return switch (usuario.getRol()) {
            case ESTUDIANTE -> new EstudiantePOVForm(usuario);
            case PROFESOR -> new ProfesorPOVForm(usuario);
            case ADMINISTRADOR -> new AdministradorPOVForm(usuario);
        };
    }
}