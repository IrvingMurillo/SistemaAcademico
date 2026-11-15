package com.sistema.vista.registrar;

import com.sistema.controlador.AutenticacionControlador;
import com.sistema.vista.LoginForm;
import com.sistema.vista.Navegador;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Parte común de los formularios de registro: campos, botones y manejo de errores.
 * Cada formulario solo dice qué etiquetas usa y qué método del controlador llama.
 */
public abstract class RegistrarBaseForm extends JPanel {
    protected final JTextField txtCodigo = new JTextField(20);
    protected final JTextField txtNombre = new JTextField(20);
    protected final JTextField txtExtra = new JTextField(20); // carrera o especialización
    protected final JTextField txtCorreo = new JTextField(20);
    protected final JPasswordField txtPassword = new JPasswordField(20);
    protected final AutenticacionControlador controlador = new AutenticacionControlador();

    /** etiquetaExtra va en null si el formulario no tiene ese campo. */
    protected RegistrarBaseForm(String etiquetaCodigo, String etiquetaExtra) {
        setLayout(new BorderLayout());

        JPanel campos = new JPanel(new GridLayout(0, 2, 10, 10));
        campos.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        agregarFila(campos, etiquetaCodigo, txtCodigo);
        agregarFila(campos, "Nombre:", txtNombre);
        if (etiquetaExtra != null) {
            agregarFila(campos, etiquetaExtra, txtExtra);
        }
        agregarFila(campos, "Correo:", txtCorreo);
        agregarFila(campos, "Contraseña:", txtPassword);

        JButton btnCancelar = new JButton("Cancelar");
        JButton btnCrear = new JButton("Crear");
        btnCancelar.addActionListener(e -> Navegador.irA(new LoginForm()));
        btnCrear.addActionListener(e -> intentarCrear());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnCancelar);
        botones.add(btnCrear);

        add(campos, BorderLayout.NORTH);
        add(botones, BorderLayout.SOUTH);
    }

    /** Cada formulario llama aquí al método de registro que le corresponde. */
    protected abstract void guardar();

    protected String password() {
        return new String(txtPassword.getPassword());
    }

    private void intentarCrear() {
        try {
            guardar();
            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            Navegador.irA(new LoginForm());
        } catch (IllegalArgumentException ex) { // datos inválidos o duplicados
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Revise los datos", JOptionPane.WARNING_MESSAGE);
        } catch (RuntimeException ex) { // problema con MongoDB
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar en la base de datos. Intente de nuevo.",
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarFila(JPanel panel, String etiqueta, JComponent campo) {
        panel.add(new JLabel(etiqueta));
        panel.add(campo);
    }
}