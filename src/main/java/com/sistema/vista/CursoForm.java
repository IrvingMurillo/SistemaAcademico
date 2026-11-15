package com.sistema.vista;

import com.sistema.controlador.CursoControlador;
import com.sistema.controlador.MatriculaControlador;
import com.sistema.modelo.Asignatura;
import com.sistema.modelo.Curso;
import com.sistema.modelo.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/** Gestión de cursos (administrador): asignatura y profesor se eligen de listas registradas. */
public class CursoForm extends BaseGestionForm {
    private final CursoControlador cursoControlador = new CursoControlador();
    private final MatriculaControlador matriculaControlador = new MatriculaControlador();

    private final JTextField txtNrc = new JTextField(10);
    private final JComboBox<Asignatura> cmbAsignatura = new JComboBox<>();
    private final JComboBox<Usuario> cmbProfesor = new JComboBox<>();
    private final JTextField txtCupos = new JTextField(5);

    public CursoForm() {
        super("NRC", "Sigla", "Asignatura", "Escalafón", "Profesor", "Cupos", "Inscritos", "Disponibles");
        armar(campos("NRC:", txtNrc, "Asignatura:", cmbAsignatura,
                        "Profesor:", cmbProfesor, "Cupos:", txtCupos),
                boton("Guardar", this::guardar),
                boton("Modificar", this::modificar),
                boton("Eliminar", this::eliminar),
                boton("Limpiar", this::limpiar));
        recargar();
    }

    private void guardar() {
        if (Mensajes.ejecutar(this, () -> cursoControlador.guardar(
                txtNrc.getText(), siglaElegida(), escalafonElegido(), txtCupos.getText()))) {
            Mensajes.info(this, "Curso guardado correctamente.");
            limpiar();
            recargar();
        }
    }

    private void modificar() {
        if (Mensajes.ejecutar(this, () -> cursoControlador.modificar(
                txtNrc.getText(), siglaElegida(), escalafonElegido(), txtCupos.getText()))) {
            Mensajes.info(this, "Curso modificado correctamente.");
            limpiar();
            recargar();
        }
    }

    private void eliminar() {
        String nrc = txtNrc.getText().trim();
        if (nrc.isEmpty()) {
            Mensajes.aviso(this, "Seleccione un curso de la tabla.");
            return;
        }
        if (!Mensajes.confirmar(this, "¿Seguro que desea eliminar el curso " + nrc + "?")) {
            return;
        }
        if (Mensajes.ejecutar(this, () -> cursoControlador.eliminar(nrc))) {
            Mensajes.info(this, "Curso eliminado correctamente.");
            limpiar();
            recargar();
        }
    }

    private void limpiar() {
        txtNrc.setText("");
        txtCupos.setText("");
        if (cmbAsignatura.getItemCount() > 0) {
            cmbAsignatura.setSelectedIndex(0);
        }
        if (cmbProfesor.getItemCount() > 0) {
            cmbProfesor.setSelectedIndex(0);
        }
        tabla.limpiarSeleccion();
    }

    /** Sigla de la asignatura elegida en el combo ("" si el combo está vacío). */
    private String siglaElegida() {
        Asignatura asignatura = (Asignatura) cmbAsignatura.getSelectedItem();
        return (asignatura == null) ? "" : asignatura.getSigla();
    }

    /** Escalafón del profesor elegido en el combo ("" si el combo está vacío). */
    private String escalafonElegido() {
        Usuario profesor = (Usuario) cmbProfesor.getSelectedItem();
        return (profesor == null) ? "" : profesor.getIdentificador();
    }

    @Override
    protected void cargarSeleccion() {
        String nrc = tabla.valorSeleccionado(0);
        if (nrc == null) {
            return;
        }
        txtNrc.setText(nrc);
        seleccionarAsignatura(tabla.valorSeleccionado(1));
        seleccionarProfesor(tabla.valorSeleccionado(3));
        txtCupos.setText(tabla.valorSeleccionado(5));
    }

    private void seleccionarAsignatura(String sigla) {
        for (int i = 0; i < cmbAsignatura.getItemCount(); i++) {
            if (cmbAsignatura.getItemAt(i).getSigla().equals(sigla)) {
                cmbAsignatura.setSelectedIndex(i);
                return;
            }
        }
    }

    private void seleccionarProfesor(String escalafon) {
        for (int i = 0; i < cmbProfesor.getItemCount(); i++) {
            if (cmbProfesor.getItemAt(i).getIdentificador().equals(escalafon)) {
                cmbProfesor.setSelectedIndex(i);
                return;
            }
        }
    }

    @Override
    protected void leerDatos() {
        cmbAsignatura.removeAllItems();
        for (Asignatura a : cursoControlador.listarAsignaturas()) {
            cmbAsignatura.addItem(a);
        }
        cmbProfesor.removeAllItems();
        for (Usuario p : cursoControlador.listarProfesores()) {
            cmbProfesor.addItem(p);
        }

        Map<String, String> asignaturas = cursoControlador.nombresDeAsignaturas();
        Map<String, String> profesores = cursoControlador.nombresDeProfesores();
        List<Object[]> filas = new ArrayList<>();
        for (Curso c : cursoControlador.listar()) {
            filas.add(new Object[]{
                c.getNrc(),
                c.getAsignatura(),
                asignaturas.getOrDefault(c.getAsignatura(), ""),
                c.getProfesor(),
                profesores.getOrDefault(c.getProfesor(), ""),
                c.getCupos(),
                matriculaControlador.contarInscritos(c.getNrc()),
                matriculaControlador.cuposDisponibles(c)});
        }
        tabla.cargar(filas);
    }
}