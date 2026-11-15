package com.sistema.vista.pov;

import com.sistema.controlador.MatriculaControlador;
import com.sistema.modelo.Curso;
import com.sistema.modelo.Matricula;
import com.sistema.modelo.Usuario;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/** El estudiante ve la oferta de cursos, se matricula y se retira. */
public class EstudiantePOVForm extends BasePOVForm {
    private final MatriculaControlador controlador = new MatriculaControlador();
    private final String carnet;

    private final DefaultTableModel modeloOferta =
            crearModelo("NRC", "Asignatura", "Profesor", "Cupos disponibles");
    private final DefaultTableModel modeloMisCursos =
            crearModelo("Código", "NRC", "Asignatura", "Fecha", "Estado");
    private final JTable tablaOferta = new JTable(modeloOferta);
    private final JTable tablaMisCursos = new JTable(modeloMisCursos);

    public EstudiantePOVForm(Usuario usuario) {
        super(usuario, "Panel del Estudiante");
        carnet = usuario.getIdentificador();

        JButton btnMatricular = new JButton("Matricularme en el curso seleccionado");
        JButton btnRetirar = new JButton("Retirarme del curso seleccionado");
        btnMatricular.addActionListener(e -> matricular());
        btnRetirar.addActionListener(e -> retirar());

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Oferta de cursos", crearPanel(tablaOferta, btnMatricular));
        pestanas.addTab("Mis cursos", crearPanel(tablaMisCursos, btnRetirar));
        contenido.add(pestanas, BorderLayout.CENTER);

        actualizar();
    }

    /** Una tabla con su botón de acción debajo. */
    private JPanel crearPanel(JTable tabla, JButton boton) {
        JPanel botones = new JPanel(new FlowLayout());
        botones.add(boton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    /** Vuelve a leer de MongoDB las dos tablas. */
    private void actualizar() {
        Map<String, String> profesores = controlador.nombresDeProfesores();
        Map<String, String> asignaturas = new HashMap<>(); // NRC -> asignatura

        modeloOferta.setRowCount(0);
        for (Curso curso : controlador.listarCursos()) {
            asignaturas.put(curso.getNrc(), curso.getAsignatura());
            modeloOferta.addRow(new Object[]{
                curso.getNrc(),
                curso.getAsignatura(),
                profesores.getOrDefault(curso.getProfesor(), curso.getProfesor()),
                controlador.cuposDisponibles(curso)});
        }

        modeloMisCursos.setRowCount(0);
        for (Matricula m : controlador.listarMatriculasDe(carnet)) {
            modeloMisCursos.addRow(new Object[]{
                m.getCodigo(), m.getNrc(), asignaturas.get(m.getNrc()), m.getFecha(), m.getEstado()});
        }
    }

    private void matricular() {
        int fila = tablaOferta.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un curso de la tabla.",
                    "Revise los datos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nrc = modeloOferta.getValueAt(fila, 0).toString();

        try {
            controlador.matricular(carnet, nrc);
            JOptionPane.showMessageDialog(this, "Matrícula realizada en el curso " + nrc + ".",
                    "Matrícula exitosa", JOptionPane.INFORMATION_MESSAGE);
            actualizar();
        } catch (IllegalArgumentException ex) { // duplicada, sin cupos, curso inexistente...
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Revise los datos", JOptionPane.WARNING_MESSAGE);
        } catch (RuntimeException ex) { // problema con MongoDB
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar en la base de datos. Intente de nuevo.",
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void retirar() {
        int fila = tablaMisCursos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una matrícula de la tabla.",
                    "Revise los datos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String codigo = modeloMisCursos.getValueAt(fila, 0).toString();
        String nrc = modeloMisCursos.getValueAt(fila, 1).toString();

        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Seguro que desea retirarse del curso " + nrc + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            controlador.retirar(codigo);
            JOptionPane.showMessageDialog(this, "Se retiró del curso " + nrc + ".",
                    "Retiro exitoso", JOptionPane.INFORMATION_MESSAGE);
            actualizar();
        } catch (IllegalArgumentException ex) { // matrícula inexistente o ya retirada
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Revise los datos", JOptionPane.WARNING_MESSAGE);
        } catch (RuntimeException ex) { // problema con MongoDB
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar en la base de datos. Intente de nuevo.",
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }
}