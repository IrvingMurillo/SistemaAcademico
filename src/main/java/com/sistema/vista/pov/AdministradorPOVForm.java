package com.sistema.vista.pov;

import com.sistema.modelo.Usuario;
import com.sistema.vista.AsignaturaForm;
import com.sistema.vista.BaseGestionForm;
import com.sistema.vista.CursoForm;
import com.sistema.vista.EstudianteForm;
import com.sistema.vista.MatriculaForm;
import com.sistema.vista.ResumenMatriculaForm;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JTabbedPane;

/** Panel del administrador: una pestaña por pantalla de gestión. */
public class AdministradorPOVForm extends BasePOVForm {

    public AdministradorPOVForm(Usuario usuario) {
        super(usuario, "Panel del Administrador");

        List<BaseGestionForm> paneles = List.of(new AsignaturaForm(), new CursoForm(),
                new EstudianteForm(), new MatriculaForm(), new ResumenMatriculaForm());
        String[] titulos = {"Asignaturas", "Cursos", "Estudiantes", "Matrículas", "Resumen de matrícula"};

        JTabbedPane pestanas = new JTabbedPane();
        for (int i = 0; i < paneles.size(); i++) {
            pestanas.addTab(titulos[i], paneles.get(i));
        }
        // Al cambiar de pestaña se vuelve a leer MongoDB: nada queda desactualizado
        pestanas.addChangeListener(e -> paneles.get(pestanas.getSelectedIndex()).recargar());

        contenido.add(pestanas, BorderLayout.CENTER);
    }
}