package com.sistema.vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * Tabla de solo lectura con buscador, orden por encabezado y contador de resultados.
 * La usan todas las pantallas de consulta.
 */
public class TablaConsulta extends JPanel {
    private final DefaultTableModel modelo;
    private final JTable tabla;
    private final TableRowSorter<DefaultTableModel> orden;
    private final JTextField txtBuscar = new JTextField(18);
    private final JLabel lblResultado = new JLabel();

    public TablaConsulta(String... columnas) {
        super(new BorderLayout(0, 5));
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }

            /** Permite ordenar los números como números y no como texto. */
            @Override
            public Class<?> getColumnClass(int columna) {
                Object valor = (getRowCount() > 0) ? getValueAt(0, columna) : null;
                return (valor == null) ? Object.class : valor.getClass();
            }
        };
        tabla = new JTable(modelo);
        orden = new TableRowSorter<>(modelo);
        tabla.setRowSorter(orden);

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barra.add(new JLabel("Buscar:"));
        barra.add(txtBuscar);
        barra.add(lblResultado);

        add(barra, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrar();
            }
        });
    }

    /** Reemplaza el contenido de la tabla con las filas dadas. */
    public void cargar(List<Object[]> filas) {
        modelo.setRowCount(0);
        for (Object[] fila : filas) {
            modelo.addRow(fila);
        }
        actualizarResultado();
    }

    /** Devuelve el valor de la columna en la fila seleccionada, o null si no hay selección. */
    public String valorSeleccionado(int columna) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return null;
        }
        // convertRowIndexToModel: la fila visible puede estar ordenada o filtrada
        return String.valueOf(modelo.getValueAt(tabla.convertRowIndexToModel(fila), columna));
    }

    /** Ejecuta la acción cada vez que cambia la fila seleccionada. */
    public void alSeleccionar(Runnable accion) {
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                accion.run();
            }
        });
    }

    public void limpiarSeleccion() {
        tabla.clearSelection();
    }

    /** Muestra solo las filas que contienen el texto escrito (sin distinguir mayúsculas). */
    private void filtrar() {
        String texto = txtBuscar.getText().trim();
        if (texto.isEmpty()) {
            orden.setRowFilter(null);
        } else {
            orden.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
        }
        actualizarResultado();
    }

    private void actualizarResultado() {
        int filas = tabla.getRowCount();
        lblResultado.setText(filas == 0 ? "No hay registros que coincidan." : filas + " registro(s)");
    }
}