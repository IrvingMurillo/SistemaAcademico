package com.sistema.vista;

import com.sistema.controlador.AsignaturaControlador;
import com.sistema.modelo.Asignatura;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextField;

/** Gestión de asignaturas (administrador): guardar, modificar, eliminar, buscar y ordenar. */
public class AsignaturaForm extends BaseGestionForm {
    private final AsignaturaControlador controlador = new AsignaturaControlador();
    private final JTextField txtSigla = new JTextField(12);
    private final JTextField txtNombre = new JTextField(25);
    private final JTextField txtValidez = new JTextField(5);

    public AsignaturaForm() {
        super("Sigla", "Nombre", "Validez (U.V.)");
        armar(campos("Sigla:", txtSigla, "Nombre:", txtNombre, "Validez (U.V.):", txtValidez),
                boton("Guardar", this::guardar),
                boton("Modificar", this::modificar),
                boton("Eliminar", this::eliminar),
                boton("Limpiar", this::limpiar));
        recargar();
    }

    private void guardar() {
        if (Mensajes.ejecutar(this, () -> controlador.guardar(
                txtSigla.getText(), txtNombre.getText(), txtValidez.getText()))) {
            Mensajes.info(this, "Asignatura guardada correctamente.");
            limpiar();
            recargar();
        }
    }

    private void modificar() {
        if (Mensajes.ejecutar(this, () -> controlador.modificar(
                txtSigla.getText(), txtNombre.getText(), txtValidez.getText()))) {
            Mensajes.info(this, "Asignatura modificada correctamente.");
            limpiar();
            recargar();
        }
    }

    private void eliminar() {
        String sigla = txtSigla.getText().trim();
        if (sigla.isEmpty()) {
            Mensajes.aviso(this, "Seleccione una asignatura de la tabla.");
            return;
        }
        if (!Mensajes.confirmar(this, "¿Seguro que desea eliminar la asignatura " + sigla + "?")) {
            return;
        }
        if (Mensajes.ejecutar(this, () -> controlador.eliminar(sigla))) {
            Mensajes.info(this, "Asignatura eliminada correctamente.");
            limpiar();
            recargar();
        }
    }

    private void limpiar() {
        txtSigla.setText("");
        txtNombre.setText("");
        txtValidez.setText("");
        tabla.limpiarSeleccion();
    }

    @Override
    protected void cargarSeleccion() {
        String sigla = tabla.valorSeleccionado(0);
        if (sigla == null) {
            return;
        }
        txtSigla.setText(sigla);
        txtNombre.setText(tabla.valorSeleccionado(1));
        txtValidez.setText(tabla.valorSeleccionado(2));
    }

    @Override
    protected void leerDatos() {
        List<Object[]> filas = new ArrayList<>();
        for (Asignatura a : controlador.listar()) {
            filas.add(new Object[]{a.getSigla(), a.getNombre(), a.getValidez()});
        }
        tabla.cargar(filas);
    }
}