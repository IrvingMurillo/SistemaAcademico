package com.sistema.controlador;

import com.sistema.modelo.Asignatura;
import com.sistema.modelo.Curso;
import com.sistema.modelo.Matricula;
import com.sistema.modelo.Rol;
import com.sistema.modelo.Usuario;
import com.sistema.persistencia.AsignaturaDAO;
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

/** Reglas de matrícula y consultas académicas. */
public class MatriculaControlador {
    /** Valor del filtro de estado que muestra todas las matrículas. */
    public static final String TODOS = "TODOS";

    private final MatriculaDAO matriculaDAO = new MatriculaDAO();
    private final CursoDAO cursoDAO = new CursoDAO();
    private final AsignaturaDAO asignaturaDAO = new AsignaturaDAO();
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

    /** Retira una matrícula activa: cambia su estado, no la borra. */
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

    /** Estudiantes con matrícula activa en el curso. */
    public int contarInscritos(String nrc) {
        return matriculaDAO.contarActivas(nrc);
    }

    /** Los cupos disponibles se calculan, no se guardan: cupos del curso menos inscritos. */
    public int cuposDisponibles(Curso curso) {
        return curso.getCupos() - contarInscritos(curso.getNrc());
    }

    /** Matrículas filtradas por estado (ACTIVA, RETIRADA o TODOS). */
    public List<Matricula> listarMatriculas(String estado) {
        return filtrarPorEstado(matriculaDAO.listarTodas(), estado);
    }

    /** Historial completo de un estudiante, con matrículas activas y retiradas. */
    public List<Matricula> listarMatriculasDe(String carnet) {
        return matriculaDAO.listarPorCarnet(carnet);
    }

    /** Solo las matrículas vigentes de un estudiante (lo que ve en "Mis cursos"). */
    public List<Matricula> listarMatriculasActivasDe(String carnet) {
        return filtrarPorEstado(matriculaDAO.listarPorCarnet(carnet), Matricula.ACTIVA);
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

    /** NRC -> "sigla - nombre de la asignatura", para mostrar en las tablas. */
    public Map<String, String> nombresDeCursos() {
        Map<String, String> asignaturas = new HashMap<>();
        for (Asignatura asignatura : asignaturaDAO.listarTodas()) {
            asignaturas.put(asignatura.getSigla(), asignatura.toString());
        }
        Map<String, String> nombres = new HashMap<>();
        for (Curso curso : cursoDAO.listarTodos()) {
            nombres.put(curso.getNrc(), asignaturas.getOrDefault(curso.getAsignatura(), curso.getAsignatura()));
        }
        return nombres;
    }

    /** Carnet -> nombre del estudiante. */
    public Map<String, String> nombresDeEstudiantes() {
        Map<String, String> nombres = new HashMap<>();
        for (Usuario estudiante : usuarioDAO.listarPorRol(Rol.ESTUDIANTE)) {
            nombres.put(estudiante.getIdentificador(), estudiante.getNombreCompleto());
        }
        return nombres;
    }

    private List<Matricula> filtrarPorEstado(List<Matricula> matriculas, String estado) {
        if (estado == null || TODOS.equals(estado)) {
            return matriculas;
        }
        List<Matricula> filtradas = new ArrayList<>();
        for (Matricula matricula : matriculas) {
            if (estado.equals(matricula.getEstado())) {
                filtradas.add(matricula);
            }
        }
        return filtradas;
    }
}