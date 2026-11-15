package com.sistema.vista;

import com.sistema.controlador.ValidadorDatos;
import com.sistema.modelo.Asignatura;
import com.sistema.modelo.Curso;
import com.sistema.modelo.Rol;
import com.sistema.modelo.Usuario;
import com.sistema.persistencia.AsignaturaDAO;
import com.sistema.persistencia.CursoDAO;
import com.sistema.persistencia.MatriculaDAO;
import com.sistema.persistencia.UsuarioDAO;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class CursoForm extends JFrame {

    private final CursoDAO cursoDAO = new CursoDAO();
    private final AsignaturaDAO asignaturaDAO = new AsignaturaDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final MatriculaDAO matriculaDAO = new MatriculaDAO();

    private final JTextField txtNrc = new JTextField(15);
    private final JComboBox<String> cmbAsignatura = new JComboBox<>();
    private final JComboBox<Usuario> cmbProfesor = new JComboBox<>();
    private final JTextField txtCupos = new JTextField(10);

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[]{"NRC", "Asignatura", "Profesor", "Cupos"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modeloTabla);

    public CursoForm() {
        super("Gestión de Cursos");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(620, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBorder(BorderFactory.createTitledBorder("Datos del Curso"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panelCampos.add(new JLabel("NRC:"), gbc);
        gbc.gridx = 1;
        panelCampos.add(txtNrc, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelCampos.add(new JLabel("Asignatura:"), gbc);
        gbc.gridx = 1;
        panelCampos.add(cmbAsignatura, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelCampos.add(new JLabel("Profesor:"), gbc);
        gbc.gridx = 1;
        panelCampos.add(cmbProfesor, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panelCampos.add(new JLabel("Cupos:"), gbc);
        gbc.gridx = 1;
        panelCampos.add(txtCupos, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.add(panelCampos, BorderLayout.CENTER);
        panelNorte.add(panelBotones, BorderLayout.SOUTH);
        add(panelNorte, BorderLayout.NORTH);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardar());
        btnModificar.addActionListener(e -> modificar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila >= 0) {
                    txtNrc.setText(modeloTabla.getValueAt(fila, 0).toString());
                    cmbAsignatura.setSelectedItem(modeloTabla.getValueAt(fila, 1).toString());
                    seleccionarProfesor(modeloTabla.getValueAt(fila, 2).toString());
                    txtCupos.setText(modeloTabla.getValueAt(fila, 3).toString());
                }
            }
        });

        cargarAsignaturas();
        cargarProfesores();
        cargarTabla();
    }

    private void cargarAsignaturas() {
        cmbAsignatura.removeAllItems();
        for (Asignatura a : asignaturaDAO.listarTodas()) {
            cmbAsignatura.addItem(a.getSigla());
        }
    }

    private void cargarProfesores() {
        cmbProfesor.removeAllItems();
        for (Usuario p : usuarioDAO.listarPorRol(Rol.PROFESOR)) {
            cmbProfesor.addItem(p);
        }
    }

    /** Selecciona en el combo al profesor con ese escalafón (el que muestra la tabla). */
    private void seleccionarProfesor(String escalafon) {
        for (int i = 0; i < cmbProfesor.getItemCount(); i++) {
            if (cmbProfesor.getItemAt(i).getIdentificador().equals(escalafon)) {
                cmbProfesor.setSelectedIndex(i);
                return;
            }
        }
    }

    private int validarCupos(String textoCupos) {
        ValidadorDatos.requerido(textoCupos, "Cupos");
        try {
            int cupos = Integer.parseInt(textoCupos.trim());
            if (cupos <= 0) {
                throw new IllegalArgumentException("El campo Cupos debe ser mayor a 0.");
            }
            return cupos;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo Cupos debe ser un número entero válido.");
        }
    }

    private void guardar() {
        try {
            String nrc = txtNrc.getText().trim().toUpperCase();
            Object seleccionado = cmbAsignatura.getSelectedItem();
            String asignatura = (seleccionado != null) ? seleccionado.toString() : "";
            Usuario profesorElegido = (Usuario) cmbProfesor.getSelectedItem();
            String profesor = (profesorElegido != null) ? profesorElegido.getIdentificador() : "";

            ValidadorDatos.requerido(nrc, "NRC");
            ValidadorDatos.requerido(asignatura, "Asignatura (registre primero una asignatura)");
            ValidadorDatos.requerido(profesor, "Profesor (registre primero un profesor)");
            int cupos = validarCupos(txtCupos.getText());

            Curso c = new Curso(nrc, asignatura, profesor, cupos);
            if (!cursoDAO.insertar(c)) {
                JOptionPane.showMessageDialog(this, "Ya existe un curso con el NRC " + nrc, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(this, "Curso guardado correctamente.");
            limpiar();
            cargarTabla();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificar() {
        try {
            String nrc = txtNrc.getText().trim().toUpperCase();
            Object seleccionado = cmbAsignatura.getSelectedItem();
            String asignatura = (seleccionado != null) ? seleccionado.toString() : "";
            Usuario profesorElegido = (Usuario) cmbProfesor.getSelectedItem();
            String profesor = (profesorElegido != null) ? profesorElegido.getIdentificador() : "";

            ValidadorDatos.requerido(nrc, "NRC");
            ValidadorDatos.requerido(asignatura, "Asignatura");
            ValidadorDatos.requerido(profesor, "Profesor");
            int cupos = validarCupos(txtCupos.getText());

            if (cupos < matriculaDAO.contarActivas(nrc)) {
                JOptionPane.showMessageDialog(this, "Los cupos no pueden ser menores a los estudiantes ya matriculados en el curso " + nrc, "Integridad", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Curso c = new Curso(nrc, asignatura, profesor, cupos);
            if (!cursoDAO.modificar(c)) {
                JOptionPane.showMessageDialog(this, "No se encontró ningún curso con el NRC " + nrc, "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(this, "Curso modificado correctamente.");
            limpiar();
            cargarTabla();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        try {
            String nrc = txtNrc.getText().trim().toUpperCase();
            ValidadorDatos.requerido(nrc, "NRC");

            if (matriculaDAO.contarActivas(nrc) > 0) {
                JOptionPane.showMessageDialog(this, "No se puede eliminar: el curso " + nrc + " tiene estudiantes matriculados.", "Integridad", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar el curso " + nrc + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (cursoDAO.eliminar(nrc)) {
                    JOptionPane.showMessageDialog(this, "Curso eliminado correctamente.");
                    limpiar();
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "No existe un curso con el NRC " + nrc, "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiar() {
        txtNrc.setText("");
        if (cmbAsignatura.getItemCount() > 0) {
            cmbAsignatura.setSelectedIndex(0);
        }
        if (cmbProfesor.getItemCount() > 0) {
            cmbProfesor.setSelectedIndex(0);
        }
        txtCupos.setText("");
        tabla.clearSelection();
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Curso c : cursoDAO.listarTodos()) {
            modeloTabla.addRow(new Object[]{c.getNrc(), c.getAsignatura(), c.getProfesor(), c.getCupos()});
        }
    }
}