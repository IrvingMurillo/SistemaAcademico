package com.sistema.vista.pov;

import com.sistema.controlador.CursoControlador;
import com.sistema.controlador.MatriculaControlador;
import com.sistema.modelo.Curso;
import com.sistema.modelo.Usuario;
import com.sistema.vista.Mensajes;
import com.sistema.vista.TablaConsulta;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/** Panel del profesor: ve sus cursos y, al elegir uno, los estudiantes matriculados. */
public class ProfesorPOVForm extends BasePOVForm {
    private final CursoControlador cursoControlador = new CursoControlador();
    private final MatriculaControlador matriculaControlador = new MatriculaControlador();
    private final String escalafon;

    private final TablaConsulta tablaCursos =
            new TablaConsulta("NRC", "Asignatura", "Cupos", "Inscritos", "Disponibles");
    private final TablaConsulta tablaEstudiantes =
            new TablaConsulta("Carnet", "Nombre", "Correo");

    public ProfesorPOVForm(Usuario usuario) {
        super(usuario, "Panel del Profesor");
        escalafon = usuario.getIdentificador();

        tablaCursos.setBorder(BorderFactory.createTitledBorder("Mis cursos (haga clic en uno)"));
        tablaEstudiantes.setBorder(BorderFactory.createTitledBorder("Estudiantes matriculados"));
        tablaCursos.alSeleccionar(this::cargarEstudiantes);

        JPanel centro = new JPanel(new GridLayout(2, 1, 0, 10));
        centro.add(tablaCursos);
        centro.add(tablaEstudiantes);
        contenido.add(centro, BorderLayout.CENTER);

        cargarCursos();
    }

    /** Lee los cursos del profesor con sus inscritos y cupos disponibles. */
    private void cargarCursos() {
        Mensajes.ejecutar(this, () -> {
            Map<String, String> nombres = matriculaControlador.nombresDeCursos();
            List<Object[]> filas = new ArrayList<>();
            for (Curso c : cursoControlador.listarCursosDe(escalafon)) {
                filas.add(new Object[]{
                    c.getNrc(),
                    nombres.getOrDefault(c.getNrc(), c.getAsignatura()),
                    c.getCupos(),
                    matriculaControlador.contarInscritos(c.getNrc()),
                    matriculaControlador.cuposDisponibles(c)});
            }
            tablaCursos.cargar(filas);
            tablaEstudiantes.cargar(new ArrayList<>());
        });
    }

    /** Lee los estudiantes con matrícula activa en el curso seleccionado. */
    private void cargarEstudiantes() {
        String nrc = tablaCursos.valorSeleccionado(0);
        if (nrc == null) {
            return;
        }
        Mensajes.ejecutar(this, () -> {
            List<Object[]> filas = new ArrayList<>();
            for (Usuario e : matriculaControlador.listarEstudiantesDe(nrc)) {
                filas.add(new Object[]{e.getIdentificador(), e.getNombreCompleto(), e.getCorreo()});
            }
            tablaEstudiantes.cargar(filas);
        });
    }
}