package com.sistema.vista;

import com.sistema.vista.registrar.RegistrarEstudianteForm;
import com.sistema.vista.registrar.RegistrarProfesorForm;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** Arriba el combo y el botón; abajo un panel que muestra el formulario elegido. */
public class RegistroForm extends JFrame {
    private static final String ESTUDIANTE = "Estudiante";
    private static final String PROFESOR = "Profesor";

    // El administrador no aparece aquí: no se puede crear desde el registro público.
    private final JComboBox<String> cmbTipo = new JComboBox<>(new String[]{ESTUDIANTE, PROFESOR});
    private final JButton btnRegistrarse = new JButton("Registrarse");
    private final CardLayout cartas = new CardLayout();
    private final JPanel panelFormularios = new JPanel(cartas);

    public RegistroForm() {
        super("Sistema Académico - Registro");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Tanto "Cancelar" como "Crear" vuelven al login
        Runnable volverAlLogin = () -> {
            new LoginForm().setVisible(true);
            dispose();
        };

        panelFormularios.add(new JPanel(), "vacio"); // panel en blanco hasta que se elija
        panelFormularios.add(new RegistrarEstudianteForm(volverAlLogin), ESTUDIANTE);
        panelFormularios.add(new RegistrarProfesorForm(volverAlLogin), PROFESOR);

        JPanel arriba = new JPanel(new FlowLayout(FlowLayout.LEFT));
        arriba.add(new JLabel("Elija el tipo:"));
        arriba.add(cmbTipo);
        arriba.add(btnRegistrarse);

        add(arriba, BorderLayout.NORTH);
        add(panelFormularios, BorderLayout.CENTER);

        btnRegistrarse.addActionListener(e ->
                cartas.show(panelFormularios, (String) cmbTipo.getSelectedItem()));

        pack();
        setLocationRelativeTo(null);
    }
}