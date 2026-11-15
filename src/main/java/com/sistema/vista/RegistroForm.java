package com.sistema.vista;

import com.sistema.vista.registrar.RegistrarEstudianteForm;
import com.sistema.vista.registrar.RegistrarProfesorForm;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** Arriba el combo y el botón; abajo un panel que muestra el formulario elegido. */
public class RegistroForm extends BaseForm {
    private static final String ESTUDIANTE = "Estudiante";
    private static final String PROFESOR = "Profesor";

    // El administrador no aparece aquí: no se puede crear desde el registro público.
    private final JComboBox<String> cmbTipo = new JComboBox<>(new String[]{ESTUDIANTE, PROFESOR});
    private final CardLayout cartas = new CardLayout();
    private final JPanel panelFormularios = new JPanel(cartas);

    public RegistroForm() {
        super("Sistema Académico - Registro");

        panelFormularios.add(new JPanel(), "vacio"); // en blanco hasta que se elija un tipo
        panelFormularios.add(new RegistrarEstudianteForm(), ESTUDIANTE);
        panelFormularios.add(new RegistrarProfesorForm(), PROFESOR);

        JButton btnRegistrarse = new JButton("Registrarse");
        btnRegistrarse.addActionListener(e ->
                cartas.show(panelFormularios, (String) cmbTipo.getSelectedItem()));

        JPanel arriba = new JPanel(new FlowLayout(FlowLayout.LEFT));
        arriba.add(new JLabel("Elija el tipo:"));
        arriba.add(cmbTipo);
        arriba.add(btnRegistrarse);

        contenido.add(arriba, BorderLayout.NORTH);
        contenido.add(panelFormularios, BorderLayout.CENTER);
    }
}