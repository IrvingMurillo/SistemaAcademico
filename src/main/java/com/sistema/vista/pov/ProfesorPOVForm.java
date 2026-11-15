package com.sistema.vista.pov;

import com.sistema.controlador.MatriculaControlador;
import com.sistema.modelo.Curso;
import com.sistema.modelo.Usuario;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/** El profesor ve sus cursos y, al elegir uno, los estudiantes matriculados. */
public class ProfesorPOVForm extends BasePOVForm {
    private final MatriculaControlador controlador = new MatriculaControlador();
    private final String escalafon;

    private final DefaultTableModel modeloCursos =
            crearModelo("NRC", "Asignatura", "Cupos", "Inscritos");
    private final DefaultTableModel modeloEstudiantes =
            crearModelo("Carnet", "Nombre", "Correo");
    private final JTable tablaCursos = new JTable(modeloCursos);
    private final JTable tablaEstudiantes = new JTable(modeloEstudiantes);

    public ProfesorPOVForm(Usuario usuario) {
        super(usuario, "Panel del Profesor");
        escalafon = usuario.getIdentificador();

        JPanel panelCursos = new JPanel(new BorderLayout());
        panelCursos.setBorder(BorderFactory.createTitledBorder("Mis cursos (haga clic en uno)"));
        panelCursos.add(new JScrollPane(tablaCursos), BorderLayout.CENTER);

        JPanel panelEstudiantes = new JPanel(new BorderLayout());
        panelEstudiantes.setBorder(BorderFactory.createTitledBorder("Estudiantes matriculados"));
        panelEstudiantes.add(new JScrollPane(tablaEstudiantes), BorderLayout.CENTER);

        JPanel centro = new JPanel(new GridLayout(2, 1, 0, 10));
        centro.add(panelCursos);
        centro.add(panelEstudiantes);
        contenido.add(centro, BorderLayout.CENTER);

        tablaCursos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cargarEstudiantes();
            }
        });

        cargarCursos();
    }

    private void cargarCursos() {
        modeloCursos.setRowCount(0);
        modeloEstudiantes.setRowCount(0);
        for (Curso curso : controlador.listarCursosDe(escalafon)) {
            modeloCursos.addRow(new Object[]{
                curso.getNrc(),
                curso.getAsignatura(),
                curso.getCupos(),
                controlador.contarInscritos(curso.getNrc())});
        }
    }

    private void cargarEstudiantes() {
        int fila = tablaCursos.getSelectedRow();
        if (fila < 0) {
            return;
        }
        String nrc = modeloCursos.getValueAt(fila, 0).toString();

        try {
            modeloEstudiantes.setRowCount(0);
            for (Usuario estudiante : controlador.listarEstudiantesDe(nrc)) {
                modeloEstudiantes.addRow(new Object[]{
                    estudiante.getIdentificador(),
                    estudiante.getNombreCompleto(),
                    estudiante.getCorreo()});
            }
        } catch (RuntimeException ex) { // problema con MongoDB
            JOptionPane.showMessageDialog(this,
                    "No se pudo leer de la base de datos. Intente de nuevo.",
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }
}