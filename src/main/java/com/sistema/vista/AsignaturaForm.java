package com.sistema.vista;

import com.sistema.controlador.ValidadorDatos;
import com.sistema.modelo.Asignatura;
import com.sistema.persistencia.AsignaturaDAO;
import com.sistema.persistencia.CursoDAO;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class AsignaturaForm extends JFrame {

    private final AsignaturaDAO asignaturaDAO = new AsignaturaDAO();
    private final CursoDAO cursoDAO = new CursoDAO();

    private final JTextField txtSigla = new JTextField(15);
    private final JTextField txtNombre = new JTextField(20);
    private final JTextField txtValidez = new JTextField(15);

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[]{"Sigla", "Nombre", "Validez"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modeloTabla);

    public AsignaturaForm() {
        super("Gestión de Asignaturas");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(580, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel superior con los campos del boceto
        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBorder(BorderFactory.createTitledBorder("Datos de la Asignatura"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panelCampos.add(new JLabel("Sigla:"), gbc);
        gbc.gridx = 1;
        panelCampos.add(txtSigla, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelCampos.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        panelCampos.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelCampos.add(new JLabel("Validez:"), gbc);
        gbc.gridx = 1;
        panelCampos.add(txtValidez, gbc);

        // Panel de botones (Guardar, Modificar, Eliminar + Limpiar)
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

        // Tabla de consulta en el centro
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Eventos de botones
        btnGuardar.addActionListener(e -> guardar());
        btnModificar.addActionListener(e -> modificar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());

        // Al hacer clic en una fila, cargar datos en los campos
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila >= 0) {
                    txtSigla.setText(modeloTabla.getValueAt(fila, 0).toString());
                    txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtValidez.setText(modeloTabla.getValueAt(fila, 2).toString());
                }
            }
        });

        cargarTabla();
    }

    private void guardar() {
        try {
            String sigla = txtSigla.getText().trim().toUpperCase();
            String nombre = txtNombre.getText().trim();
            String validez = txtValidez.getText().trim();

            ValidadorDatos.requerido(sigla, "Sigla");
            ValidadorDatos.requerido(nombre, "Nombre");
            ValidadorDatos.requerido(validez, "Validez");

            Asignatura a = new Asignatura(sigla, nombre, validez);
            if (!asignaturaDAO.insertar(a)) {
                JOptionPane.showMessageDialog(this, "Ya existe una asignatura con la sigla " + sigla, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(this, "Asignatura guardada correctamente.");
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
            String sigla = txtSigla.getText().trim().toUpperCase();
            String nombre = txtNombre.getText().trim();
            String validez = txtValidez.getText().trim();

            ValidadorDatos.requerido(sigla, "Sigla");
            ValidadorDatos.requerido(nombre, "Nombre");
            ValidadorDatos.requerido(validez, "Validez");

            Asignatura a = new Asignatura(sigla, nombre, validez);
            if (!asignaturaDAO.modificar(a)) {
                JOptionPane.showMessageDialog(this, "No se encontró ninguna asignatura con la sigla " + sigla, "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(this, "Asignatura modificada correctamente.");
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
            String sigla = txtSigla.getText().trim().toUpperCase();
            ValidadorDatos.requerido(sigla, "Sigla");

            if (cursoDAO.existeCursoConAsignatura(sigla)) {
                JOptionPane.showMessageDialog(this, "No se puede eliminar: existen cursos asociados a la asignatura " + sigla, "Integridad", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar la asignatura " + sigla + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (asignaturaDAO.eliminar(sigla)) {
                    JOptionPane.showMessageDialog(this, "Asignatura eliminada correctamente.");
                    limpiar();
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "No existe una asignatura con la sigla " + sigla, "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiar() {
        txtSigla.setText("");
        txtNombre.setText("");
        txtValidez.setText("");
        tabla.clearSelection();
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Asignatura a : asignaturaDAO.listarTodas()) {
            modeloTabla.addRow(new Object[]{a.getSigla(), a.getNombre(), a.getValidez()});
        }
    }
}


