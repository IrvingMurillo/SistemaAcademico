package com.sistema.controlador;

import com.sistema.modelo.Curso;
import com.sistema.modelo.Matricula;
import com.sistema.modelo.Rol;
import com.sistema.modelo.Usuario;
import com.sistema.persistencia.CursoDAO;
import com.sistema.persistencia.EstudianteDAO;
import com.sistema.persistencia.MatriculaDAO;
import com.sistema.persistencia.UsuarioDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Reglas de matrícula y consultas de los POV de estudiante y profesor. */
public class MatriculaControlador {
    private final MatriculaDAO matriculaDAO = new MatriculaDAO();
    private final CursoDAO cursoDAO = new CursoDAO();
    private final EstudianteDAO estudianteDAO = new EstudianteDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /** Matricula al estudiante si todo es válido; si no, lanza un error con el motivo. */
    public void matricular(String carnet, String nrc) {
        ValidadorDatos.requerido(carnet, "carnet");
        ValidadorDatos.requerido(nrc, "NRC");

        if (estudianteDAO.buscarPorCarnet(carnet) == null) {
            throw new IllegalArgumentException("El estudiante no existe.");
        }
        Curso curso = cursoDAO.buscarPorNrc(nrc);
        if (curso == null) {
            throw new IllegalArgumentException("El curso " + nrc + " no existe.");
        }
        if (matriculaDAO.existeActiva(carnet, nrc)) {
            throw new IllegalArgumentException("Ya está matriculado en el curso " + nrc + ".");
        }
        if (cuposDisponibles(curso) <= 0) {
            throw new IllegalArgumentException("El curso " + nrc + " no tiene cupos disponibles.");
        }

        matriculaDAO.insertar(new Matricula(matriculaDAO.siguienteCodigo(), carnet, nrc,
                LocalDate.now().toString(), Matricula.ACTIVA));
    }

    public void retirar(String codigo) {
        ValidadorDatos.requerido(codigo, "matrícula");

        Matricula matricula = matriculaDAO.buscarPorCodigo(codigo);
        if (matricula == null) {
            throw new IllegalArgumentException("La matrícula no existe.");
        }
        if (!Matricula.ACTIVA.equals(matricula.getEstado())) {
            throw new IllegalArgumentException("La matrícula ya fue retirada.");
        }
        matriculaDAO.retirar(codigo);
    }

    public int contarInscritos(String nrc) {
        return matriculaDAO.contarActivas(nrc);
    }

    /** Los cupos disponibles se calculan, no se guardan: cupos del curso menos inscritos. */
    public int cuposDisponibles(Curso curso) {
        return curso.getCupos() - contarInscritos(curso.getNrc());
    }

    public List<Curso> listarCursos() {
        return cursoDAO.listarTodos();
    }

    public List<Curso> listarCursosDe(String escalafon) {
        List<Curso> propios = new ArrayList<>();
        for (Curso curso : cursoDAO.listarTodos()) {
            if (escalafon.equals(curso.getProfesor())) {
                propios.add(curso);
            }
        }
        return propios;
    }

    public List<Matricula> listarMatriculasDe(String carnet) {
        return matriculaDAO.listarPorCarnet(carnet);
    }

    /** Estudiantes con matrícula activa en el curso. */
    public List<Usuario> listarEstudiantesDe(String nrc) {
        Set<String> carnets = new HashSet<>();
        for (Matricula matricula : matriculaDAO.listarActivasPorNrc(nrc)) {
            carnets.add(matricula.getCarnet());
        }
        List<Usuario> estudiantes = new ArrayList<>();
        for (Usuario usuario : usuarioDAO.listarPorRol(Rol.ESTUDIANTE)) {
            if (carnets.contains(usuario.getIdentificador())) {
                estudiantes.add(usuario);
            }
        }
        return estudiantes;
    }

    /** Escalafón -> nombre, para mostrar el nombre del profesor en las tablas. */
    public Map<String, String> nombresDeProfesores() {
        Map<String, String> nombres = new HashMap<>();
        for (Usuario profesor : usuarioDAO.listarPorRol(Rol.PROFESOR)) {
            nombres.put(profesor.getIdentificador(), profesor.getNombreCompleto());
        }
        return nombres;
    }
}