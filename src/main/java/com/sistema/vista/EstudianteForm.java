package com.sistema.vista;

import com.sistema.controlador.EstudianteControlador;
import com.sistema.controlador.MatriculaControlador;
import com.sistema.modelo.Matricula;
import com.sistema.modelo.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JTextField;

/**
 * Gestión de estudiantes (administrador): buscar, modificar, eliminar y ver la información
 * académica (matrículas) del estudiante seleccionado.
 */
public class EstudianteForm extends BaseGestionForm {
    private final EstudianteControlador controlador = new EstudianteControlador();
    private final MatriculaControlador matriculaControlador = new MatriculaControlador();

    private final JTextField txtCarnet = new JTextField(12);
    private final JTextField txtNombre = new JTextField(25);
    private final JTextField txtCarrera = new JTextField(25);
    private final JTextField txtCorreo = new JTextField(25);
    private final TablaConsulta tablaMatriculas =
            new TablaConsulta("Código", "NRC", "Asignatura", "Fecha", "Estado");

    public EstudianteForm() {
        super("Carnet", "Nombre", "Carrera", "Correo");
        txtCarnet.setEditable(false); // el carnet es la llave: se elige desde la tabla
        armar(campos("Carnet:", txtCarnet, "Nombre:", txtNombre,
                        "Carrera:", txtCarrera, "Correo:", txtCorreo),
                boton("Modificar", this::modificar),
                boton("Eliminar", this::eliminar),
                boton("Limpiar", this::limpiar));
        tablaMatriculas.setBorder(
                BorderFactory.createTitledBorder("Información académica del estudiante seleccionado"));
        centro.add(tablaMatriculas);
        recargar();
    }

    private void modificar() {
        if (txtCarnet.getText().isEmpty()) {
            Mensajes.aviso(this, "Seleccione un estudiante de la tabla.");
            return;
        }
        if (Mensajes.ejecutar(this, () -> controlador.modificar(txtCarnet.getText(),
                txtNombre.getText(), txtCarrera.getText(), txtCorreo.getText()))) {
            Mensajes.info(this, "Estudiante modificado correctamente.");
            limpiar();
            recargar();
        }
    }

    private void eliminar() {
        String carnet = txtCarnet.getText();
        if (carnet.isEmpty()) {
            Mensajes.aviso(this, "Seleccione un estudiante de la tabla.");
            return;
        }
        if (!Mensajes.confirmar(this, "¿Seguro que desea eliminar al estudiante " + carnet + "?")) {
            return;
        }
        if (Mensajes.ejecutar(this, () -> controlador.eliminar(carnet))) {
            Mensajes.info(this, "Estudiante eliminado correctamente.");
            limpiar();
            recargar();
        }
    }

    private void limpiar() {
        txtCarnet.setText("");
        txtNombre.setText("");
        txtCarrera.setText("");
        txtCorreo.setText("");
        tabla.limpiarSeleccion();
        tablaMatriculas.cargar(new ArrayList<>());
    }

    @Override
    protected void cargarSeleccion() {
        String carnet = tabla.valorSeleccionado(0);
        if (carnet == null) {
            return;
        }
        txtCarnet.setText(carnet);
        txtNombre.setText(tabla.valorSeleccionado(1));
        txtCarrera.setText(tabla.valorSeleccionado(2));
        txtCorreo.setText(tabla.valorSeleccionado(3));
        Mensajes.ejecutar(this, () -> cargarMatriculas(carnet));
    }

    /** Muestra el historial de matrículas del estudiante (activas y retiradas). */
    private void cargarMatriculas(String carnet) {
        Map<String, String> cursos = matriculaControlador.nombresDeCursos();
        List<Object[]> filas = new ArrayList<>();
        for (Matricula m : matriculaControlador.listarMatriculasDe(carnet)) {
            filas.add(new Object[]{m.getCodigo(), m.getNrc(),
                cursos.getOrDefault(m.getNrc(), ""), m.getFecha(), m.getEstado()});
        }
        tablaMatriculas.cargar(filas);
    }

    @Override
    protected void leerDatos() {
        Map<String, String> carreras = controlador.carreras();
        List<Object[]> filas = new ArrayList<>();
        for (Usuario u : controlador.listar()) {
            filas.add(new Object[]{u.getIdentificador(), u.getNombreCompleto(),
                carreras.getOrDefault(u.getIdentificador(), ""), u.getCorreo()});
        }
        tabla.cargar(filas);
    }
}