package com.sistema.vista.pov;

import com.sistema.modelo.Usuario;
import com.sistema.vista.AsignaturaForm;
import com.sistema.vista.CursoForm;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class AdministradorPOVForm extends BasePOVForm {

    public AdministradorPOVForm(Usuario usuario) {
        super(usuario, "Panel del Administrador");

        JPanel panelMenu = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnAsignaturas = new JButton("Gestionar Asignaturas");
        JButton btnCursos = new JButton("Gestionar Cursos");

        btnAsignaturas.addActionListener(e -> new AsignaturaForm().setVisible(true));
        btnCursos.addActionListener(e -> new CursoForm().setVisible(true));

        panelMenu.add(btnAsignaturas);
        panelMenu.add(btnCursos);

        contenido.add(panelMenu, BorderLayout.NORTH);
    }
}