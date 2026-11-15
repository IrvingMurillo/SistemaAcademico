package com.sistema.vista;

import com.sistema.controlador.MatriculaControlador;
import com.sistema.modelo.Matricula;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JComboBox;

/**
 * Consulta de matrículas (administrador): filtra por estado, busca por código, estudiante o
 * curso, y permite retirar una matrícula.
 */
public class MatriculaForm extends BaseGestionForm {
    private final MatriculaControlador controlador = new MatriculaControlador();
    private final JComboBox<String> cmbEstado = new JComboBox<>(
            new String[]{MatriculaControlador.TODOS, Matricula.ACTIVA, Matricula.RETIRADA});

    public MatriculaForm() {
        super("Código", "Carnet", "Estudiante", "NRC", "Asignatura", "Fecha", "Estado");
        armar(campos("Estado:", cmbEstado),
                boton("Retirar matrícula seleccionada", this::retirar));
        cmbEstado.addActionListener(e -> recargar());
        recargar();
    }

    private void retirar() {
        String codigo = tabla.valorSeleccionado(0);
        if (codigo == null) {
            Mensajes.aviso(this, "Seleccione una matrícula de la tabla.");
            return;
        }
        if (!Mensajes.confirmar(this, "¿Seguro que desea retirar la matrícula " + codigo + "?")) {
            return;
        }
        if (Mensajes.ejecutar(this, () -> controlador.retirar(codigo))) {
            Mensajes.info(this, "Matrícula retirada correctamente.");
            recargar();
        }
    }

    @Override
    protected void cargarSeleccion() {
        // Esta pantalla no tiene campos que rellenar: solo se elige la fila para retirarla.
    }

    @Override
    protected void leerDatos() {
        Map<String, String> estudiantes = controlador.nombresDeEstudiantes();
        Map<String, String> cursos = controlador.nombresDeCursos();
        List<Object[]> filas = new ArrayList<>();
        for (Matricula m : controlador.listarMatriculas((String) cmbEstado.getSelectedItem())) {
            filas.add(new Object[]{
                m.getCodigo(),
                m.getCarnet(),
                estudiantes.getOrDefault(m.getCarnet(), ""),
                m.getNrc(),
                cursos.getOrDefault(m.getNrc(), ""),
                m.getFecha(),
                m.getEstado()});
        }
        tabla.cargar(filas);
    }
}