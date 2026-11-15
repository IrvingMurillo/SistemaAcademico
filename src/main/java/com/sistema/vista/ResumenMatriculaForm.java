package com.sistema.vista;

import com.sistema.controlador.CursoControlador;
import com.sistema.controlador.MatriculaControlador;
import com.sistema.modelo.Curso;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Resumen de matrícula (administrador): inscritos y cupos disponibles de cada curso. */
public class ResumenMatriculaForm extends BaseGestionForm {
    private final CursoControlador cursoControlador = new CursoControlador();
    private final MatriculaControlador matriculaControlador = new MatriculaControlador();

    public ResumenMatriculaForm() {
        super("NRC", "Asignatura", "Profesor", "Cupos", "Inscritos", "Disponibles");
        armar(campos(), new javax.swing.JButton[0]);
        recargar();
    }

    @Override
    protected void cargarSeleccion() {
        // Pantalla de solo lectura: no hay campos que rellenar.
    }

    @Override
    protected void leerDatos() {
        Map<String, String> asignaturas = matriculaControlador.nombresDeCursos();
        Map<String, String> profesores = cursoControlador.nombresDeProfesores();
        List<Object[]> filas = new ArrayList<>();
        for (Curso c : cursoControlador.listar()) {
            filas.add(new Object[]{
                c.getNrc(),
                asignaturas.getOrDefault(c.getNrc(), c.getAsignatura()),
                profesores.getOrDefault(c.getProfesor(), c.getProfesor()),
                c.getCupos(),
                matriculaControlador.contarInscritos(c.getNrc()),
                matriculaControlador.cuposDisponibles(c)});
        }
        tabla.cargar(filas);
    }
}