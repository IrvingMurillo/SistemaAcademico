package com.sistema.vista.pov;

import com.sistema.controlador.CursoControlador;
import com.sistema.controlador.MatriculaControlador;
import com.sistema.modelo.Curso;
import com.sistema.modelo.Matricula;
import com.sistema.modelo.Usuario;
import com.sistema.vista.Mensajes;
import com.sistema.vista.TablaConsulta;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/** Panel del estudiante: ve la oferta de cursos, se matricula y se retira. */
public class EstudiantePOVForm extends BasePOVForm {
    private final CursoControlador cursoControlador = new CursoControlador();
    private final MatriculaControlador matriculaControlador = new MatriculaControlador();
    private final String carnet;

    private final TablaConsulta tablaOferta =
            new TablaConsulta("NRC", "Asignatura", "Profesor", "Cupos disponibles", "Matriculado");
    private final TablaConsulta tablaMisCursos =
            new TablaConsulta("Código", "NRC", "Asignatura", "Fecha");

    public EstudiantePOVForm(Usuario usuario) {
        super(usuario, "Panel del Estudiante");
        carnet = usuario.getIdentificador();

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Oferta de cursos",
                crearPanel(tablaOferta, "Matricularme en el curso seleccionado", this::matricular));
        pestanas.addTab("Mis cursos",
                crearPanel(tablaMisCursos, "Retirarme del curso seleccionado", this::retirar));
        pestanas.addChangeListener(e -> actualizar());

        contenido.add(pestanas, BorderLayout.CENTER);
        actualizar();
    }

    /** Una tabla con su botón de acción debajo. */
    private JPanel crearPanel(TablaConsulta tabla, String textoBoton, Runnable accion) {
        JButton boton = new JButton(textoBoton);
        boton.addActionListener(e -> accion.run());

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(boton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(tabla, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    /** Vuelve a leer de MongoDB las dos tablas. "Mis cursos" muestra solo matrículas activas. */
    private void actualizar() {
        Mensajes.ejecutar(this, () -> {
            Map<String, String> cursos = matriculaControlador.nombresDeCursos();
            Map<String, String> profesores = cursoControlador.nombresDeProfesores();

            Set<String> matriculados = new HashSet<>(); // NRC de los cursos donde está inscrito
            List<Object[]> misCursos = new ArrayList<>();
            for (Matricula m : matriculaControlador.listarMatriculasActivasDe(carnet)) {
                matriculados.add(m.getNrc());
                misCursos.add(new Object[]{m.getCodigo(), m.getNrc(),
                    cursos.getOrDefault(m.getNrc(), ""), m.getFecha()});
            }

            List<Object[]> oferta = new ArrayList<>();
            for (Curso c : cursoControlador.listar()) {
                oferta.add(new Object[]{
                    c.getNrc(),
                    cursos.getOrDefault(c.getNrc(), c.getAsignatura()),
                    profesores.getOrDefault(c.getProfesor(), c.getProfesor()),
                    matriculaControlador.cuposDisponibles(c),
                    matriculados.contains(c.getNrc()) ? "Sí" : "No"});
            }

            tablaOferta.cargar(oferta);
            tablaMisCursos.cargar(misCursos);
        });
    }

    private void matricular() {
        String nrc = tablaOferta.valorSeleccionado(0);
        if (nrc == null) {
            Mensajes.aviso(this, "Seleccione un curso de la tabla.");
            return;
        }
        if (Mensajes.ejecutar(this, () -> matriculaControlador.matricular(carnet, nrc))) {
            Mensajes.info(this, "Matrícula realizada en el curso " + nrc + ".");
            actualizar();
        }
    }

    private void retirar() {
        String codigo = tablaMisCursos.valorSeleccionado(0);
        if (codigo == null) {
            Mensajes.aviso(this, "Seleccione un curso de la tabla.");
            return;
        }
        String nrc = tablaMisCursos.valorSeleccionado(1);
        if (!Mensajes.confirmar(this, "¿Seguro que desea retirarse del curso " + nrc + "?")) {
            return;
        }
        if (Mensajes.ejecutar(this, () -> matriculaControlador.retirar(codigo))) {
            Mensajes.info(this, "Se retiró del curso " + nrc + ".");
            actualizar();
        }
    }
}